package com.example.checkout.order.domain.event;

import com.example.checkout.shared.domain.Event;

import com.example.checkout.order.domain.OrderStatus;
import java.time.Instant;
import java.util.UUID;

public record OrderCanceled(UUID eventId, Instant occurredAt, UUID orderId, OrderStatus previousStatus) implements Event {
    public OrderCanceled(UUID orderId, OrderStatus previousStatus) {
        this(UUID.randomUUID(), Instant.now(), orderId, previousStatus);
    }
}
