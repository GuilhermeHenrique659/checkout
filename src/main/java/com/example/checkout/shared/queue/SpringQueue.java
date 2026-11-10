package com.example.checkout.shared.queue;

import com.example.checkout.shared.domain.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ConditionalOnProperty(
        name = "app.queue.provider",
        havingValue = "spring",
        matchIfMissing = true
)
public class SpringQueue implements Queue {
    private final ApplicationEventPublisher publisher;
    private final Map<Class<?>, List<Subscriber<?>>> subscribers = new ConcurrentHashMap<>();

    public SpringQueue(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public <T extends Event> void publish(T event) {
        publisher.publishEvent(event);
    }

    @Override
    public <T extends Event> void subscriber(
            Class<T> eventType,
            Subscriber<T> subscriber
    ) {
        subscribers
                .computeIfAbsent(
                        eventType,
                        key -> new CopyOnWriteArrayList<>()
                )
                .add(subscriber);
    }

    @EventListener
    public void dispach(Event event) {
        var registered = subscribers.get(event.getClass());

        if (registered == null) return;

        registered.forEach(subscriber -> invoke(subscriber, event));
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void invoke(
            Subscriber<?> subscriber,
            Event event
    ) {
        ((Subscriber<T>) subscriber).handler((T) event);
    }
}
