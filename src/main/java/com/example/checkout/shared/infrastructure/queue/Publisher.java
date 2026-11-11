package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;

public interface Publisher {
    <T extends Event> void publish(T event);
}
