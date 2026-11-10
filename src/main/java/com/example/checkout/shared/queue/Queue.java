package com.example.checkout.shared.queue;

import com.example.checkout.shared.domain.Event;

public interface Queue {
    <T extends Event> void publish(T event);

    <T extends Event> void subscriber(Class<T> event, Subscriber<T> sub);
}
