package com.example.checkout.order.domain;

import com.example.checkout.shared.domain.DomainError;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * A change of the order status: the order entered {@code status} at {@code occurredAt}.
 * Changes form a singly linked list, each pointing to the change that followed it.
 * Immutable except for the {@code next} pointer, which can be set exactly once,
 * and only by the {@link Order} aggregate when a new status is appended.
 */
public class OrderHistory {
    private final UUID id;
    private final OrderStatus status;
    private final Instant occurredAt;
    private UUID next;

    public OrderHistory(UUID id, OrderStatus status, Instant occurredAt, UUID next) {
        this.id = id;
        this.status = status;
        this.occurredAt = occurredAt;
        this.next = next;
    }

    static OrderHistory create(OrderStatus status) {
        return new OrderHistory(UUID.randomUUID(), status, Instant.now(), null);
    }

    void linkTo(OrderHistory nextChange) {
        if (next != null) {
            throw new DomainError("STATUS_ALREADY_LINKED", "Status change already points to a next status");
        }
        this.next = nextChange.id();
    }

    public UUID id() {
        return id;
    }

    public OrderStatus status() {
        return status;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public Optional<UUID> next() {
        return Optional.ofNullable(next);
    }

    public boolean isCurrent() {
        return next == null;
    }
}
