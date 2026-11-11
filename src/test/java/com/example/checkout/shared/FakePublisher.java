package com.example.checkout.shared;

import com.example.checkout.shared.domain.Event;
import com.example.checkout.shared.infrastructure.queue.EventBus;
import com.example.checkout.shared.infrastructure.queue.Publisher;
import com.example.checkout.shared.infrastructure.queue.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePublisher implements Publisher, EventBus {

    private final Map<Class<?>, List<Subscriber<?>>> subscribers =
            new HashMap<>();

    private final List<Event> events = new ArrayList<>();

    @Override
    public <T extends Event> void publish(T event) {
        events.add(event);

        var registered = subscribers.get(event.getClass());

        if (registered == null) return;

        registered.forEach(sub -> invoke(sub, event));
    }

    @Override
    public <T extends Event> void subscribe(
            Class<T> eventType,
            Subscriber<T> sub
    ) {
        subscribers
                .computeIfAbsent(eventType, key -> new ArrayList<>())
                .add(sub);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void invoke(
            Subscriber<?> subscriber,
            Event event
    ) {
        ((Subscriber<T>) subscriber).handler((T) event);
    }

    public List<Event> events() {
        return List.copyOf(events);
    }
}