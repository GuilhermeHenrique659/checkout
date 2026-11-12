package com.example.checkout.shared.infrastructure.queue.outbox;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxModel {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column
    private String ack;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column
    private Instant processedAt;

    protected OutboxModel() {
    }

    public OutboxModel(UUID id, String eventType, String payload, String ack, Instant createdAt, Instant processedAt) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.ack = ack;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }
}
