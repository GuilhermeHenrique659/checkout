package com.example.checkout.shared.infrastructure.queue.outbox;

public enum OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
}
