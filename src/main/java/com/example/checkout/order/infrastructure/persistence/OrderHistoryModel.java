package com.example.checkout.order.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_history")
public class OrderHistoryModel {
    @Id
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID orderId;

    @Column(nullable = false, updatable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    @Column
    private UUID nextId;

    protected OrderHistoryModel() {
    }

    public OrderHistoryModel(UUID id, UUID orderId, String status, Instant occurredAt, UUID nextId) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.occurredAt = occurredAt;
        this.nextId = nextId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public UUID getNextId() {
        return nextId;
    }
}
