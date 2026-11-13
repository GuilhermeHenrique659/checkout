package com.example.checkout.shipment.domain;

import com.example.checkout.shared.domain.Event;
import com.example.checkout.shipment.domain.event.ShipmentCreated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Shipment {
    private final UUID id;
    private final UUID orderId;
    private final UUID routeId;
    private final String trackingCode;
    private ShipmentStatus status;
    private final List<Event> events = new ArrayList<>();

    public Shipment(UUID id, UUID orderId, UUID routeId, String trackingCode, ShipmentStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.routeId = routeId;
        this.trackingCode = trackingCode;
        this.status = status;
    }

    public static Shipment create(UUID orderId, Route route) {
        var shipment = new Shipment(UUID.randomUUID(), orderId, route.id(), generateTrackingCode(), ShipmentStatus.CREATED);
        shipment.events.add(new ShipmentCreated(shipment.id, orderId, shipment.trackingCode));
        return shipment;
    }

    private static String generateTrackingCode() {
        return "TRK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    public UUID id() {
        return id;
    }

    public UUID orderId() {
        return orderId;
    }

    public UUID routeId() {
        return routeId;
    }

    public String trackingCode() {
        return trackingCode;
    }

    public ShipmentStatus status() {
        return status;
    }

    public List<Event> events() {
        return List.copyOf(events);
    }
}
