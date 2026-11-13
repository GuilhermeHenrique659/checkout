package com.example.checkout.shipment.domain.event;

import com.example.checkout.shared.domain.Event;

import java.time.Instant;
import java.util.UUID;

public record ShipmentCreated(UUID eventId, Instant occurredAt, UUID shipmentId, UUID orderId, String trackingCode) implements Event {
    public ShipmentCreated(UUID shipmentId, UUID orderId, String trackingCode) {
        this(UUID.randomUUID(), Instant.now(), shipmentId, orderId, trackingCode);
    }
}
