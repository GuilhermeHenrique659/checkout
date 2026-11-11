package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;

@FunctionalInterface
public interface Subscriber<T extends Event> {
    void handler(T event);
}
