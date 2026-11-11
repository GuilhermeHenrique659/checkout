package com.example.checkout.order.domain.event;

import com.example.checkout.shared.domain.Event;

import java.time.Instant;
import java.util.UUID;

public record OrderCanceled(UUID eventId, Instant occurredAt, UUID orderId) implements Event {
    public OrderCanceled(UUID orderId) {
        this(UUID.randomUUID(), Instant.now(), orderId);
    }
}
