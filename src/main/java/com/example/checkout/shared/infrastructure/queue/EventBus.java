package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;

public interface EventBus {
    <T extends Event> void subscribe(Class<T> eventType, Subscriber<T> subscriber);
}
