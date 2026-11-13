package com.example.checkout.shipment.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "shipments")
public class ShipmentModel {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID routeId;

    @Column(nullable = false)
    private String trackingCode;

    @Column(nullable = false)
    private String status;

    protected ShipmentModel() {
    }

    public ShipmentModel(UUID id, UUID orderId, UUID routeId, String trackingCode, String status) {
        this.id = id;
        this.orderId = orderId;
        this.routeId = routeId;
        this.trackingCode = trackingCode;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getRouteId() {
        return routeId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getStatus() {
        return status;
    }
}
