package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;
import com.example.checkout.shared.infrastructure.queue.outbox.OutboxDao;
import com.example.checkout.shared.infrastructure.queue.outbox.OutboxModel;
import com.example.checkout.shared.infrastructure.queue.outbox.OutboxStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@ConditionalOnProperty(
        name = "app.publisher.provider",
        havingValue = "outbox"
)
public class OutboxPublisher implements Publisher {
    private final OutboxDao outboxDao;

    public OutboxPublisher(OutboxDao outboxDao) {
        this.outboxDao = outboxDao;
    }

    @Override
    public <T extends Event> void publish(T event) {
        var eventModel = new OutboxModel(
                event.eventId(),
                event.getClass().getSimpleName(),
                new ObjectMapper().writeValueAsString(event),
                OutboxStatus.PENDING,
                event.occurredAt(),
                null
        );

        outboxDao.save(eventModel);
    }
}
