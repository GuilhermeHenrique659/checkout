package com.example.checkout.order.domain.event;

import com.example.checkout.shared.domain.Event;

import java.time.Instant;
import java.util.UUID;

public record OrderCanceled(UUID eventId, Instant occurredAt, UUID orderId, String previousStatus) implements Event {
    public OrderCanceled(UUID orderId, String previousStatus) {
        this(UUID.randomUUID(), Instant.now(), orderId, previousStatus);
    }
}
