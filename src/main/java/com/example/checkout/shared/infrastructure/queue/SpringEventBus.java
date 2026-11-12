package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ConditionalOnProperty(
        name = "app.eventBus.provider",
        havingValue = "spring",
        matchIfMissing = true
)
public class SpringEventBus implements EventBus {
    private final Map<Class<?>, List<Subscriber<?>>> subscribers = new ConcurrentHashMap<>();

    @Override
    public <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber) {
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
