package com.example.checkout.order.domain.event;

import com.example.checkout.shared.domain.Event;

import java.time.Instant;
import java.util.UUID;

public record OrderCreated(UUID eventId, UUID orderId, Instant occurredAt) implements Event {
    public OrderCreated(UUID orderId) {
        this(UUID.randomUUID(), orderId, Instant.now());
    }
}
