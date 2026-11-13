package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.domain.DomainError;
import com.example.checkout.shared.domain.Event;
import com.example.checkout.shared.infrastructure.queue.outbox.OutboxDao;
import com.example.checkout.shared.infrastructure.queue.outbox.OutboxStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ConditionalOnProperty(
        name = "app.eventBus.provider",
        havingValue = "outbox"
)
public class OutboxEventBus implements EventBus {
    private static final Logger logger = LoggerFactory.getLogger(OutboxEventBus.class);

    private final OutboxDao outboxDao;
    private final TransactionTemplate transaction;
    private final int batchSize;
    private final int maxAttempts;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // eventType (simple class name, as written by OutboxPublisher) -> event class
    private final Map<String, Class<? extends Event>> eventTypes = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Subscriber<?>>> subscribers = new ConcurrentHashMap<>();

    public OutboxEventBus(
            OutboxDao outboxDao,
            TransactionTemplate transaction,
            @Value("${app.outbox.batch-size:100}") int batchSize,
            @Value("${app.outbox.max-attempts:10}") int maxAttempts
    ) {
        this.outboxDao = outboxDao;
        this.transaction = transaction;
        this.batchSize = batchSize;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber) {
        eventTypes.put(eventType.getSimpleName(), eventType);
        subscribers
                .computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>())
                .add(subscriber);
    }

    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:5000}")
    public void poll() {
        if (eventTypes.isEmpty()) return;

        for (int i = 0; i < batchSize; i++) {
            var result = transaction.execute(this::processNext);
            if (result == null) return; // nothing pending (or all rows locked by other pollers)
            if (result.failed()) registerFailure(result.id(), result.retryable());
        }
    }

    // Runs in one transaction: lock row -> dispatch -> mark PROCESSED -> commit (releases lock).
    // Subscribers run in the same transaction, so events they publish to the outbox commit atomically with the ack.
    private Result processNext(TransactionStatus status) {
        var model = outboxDao.lockNext(OutboxStatus.PENDING.name(), eventTypes.keySet()).orElse(null);
        if (model == null) return null;

        try {
            var eventClass = eventTypes.get(model.getEventType());
            Event event = objectMapper.readValue(model.getPayload(), eventClass);
            subscribers.getOrDefault(eventClass, List.of())
                    .forEach(subscriber -> invoke(subscriber, event));

            model.setAck(OutboxStatus.PROCESSED);
            model.setProcessedAt(Instant.now());
            outboxDao.save(model);
            return new Result(model.getId(), false, false);
        } catch (Exception e) {
            logger.error("Failed to process outbox event {} ({})", model.getId(), model.getEventType(), e);
            status.setRollbackOnly(); // discard partial subscriber work
            return new Result(model.getId(), true, isRetryable(e));
        }
    }

    // Separate transaction, after the failed one rolled back and released the row lock.
    // Exponential backoff: 1s, 2s, 4s, 8s... until maxAttempts, then the event is marked FAILED for good.
    // Business errors are not retried: the same input would fail the same way.
    private void registerFailure(UUID id, boolean retryable) {
        transaction.executeWithoutResult(status -> outboxDao.findById(id).ifPresent(model -> {
            int attempts = model.getAttempts() + 1;
            model.setAttempts(attempts);

            if (!retryable) {
                logger.warn("Outbox event {} failed with a business error, not retrying", id);
                model.setAck(OutboxStatus.FAILED);
                model.setProcessedAt(Instant.now());
                model.setNextAttemptAt(null);
            } else if (attempts >= maxAttempts) {
                logger.error("Outbox event {} failed after {} attempts, giving up", id, attempts);
                model.setAck(OutboxStatus.FAILED);
                model.setProcessedAt(Instant.now());
                model.setNextAttemptAt(null);
            } else {
                var delay = backoff(attempts);
                logger.warn("Outbox event {} failed (attempt {}/{}), retrying in {}", id, attempts, maxAttempts, delay);
                model.setNextAttemptAt(Instant.now().plus(delay));
            }

            outboxDao.save(model);
        }));
    }

    // DomainError / ApplicationError are business rule violations (e.g. PRODUCT_UNAVAILABLE): retrying won't help.
    static boolean isRetryable(Exception e) {
        return !(e instanceof DomainError || e instanceof ApplicationError);
    }

    static Duration backoff(int attempts) {
        return Duration.ofSeconds(1L << Math.min(attempts - 1, 30));
    }

    private record Result(UUID id, boolean failed, boolean retryable) {
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void invoke(Subscriber<?> subscriber, Event event) {
        ((Subscriber<T>) subscriber).handler((T) event);
    }
}
