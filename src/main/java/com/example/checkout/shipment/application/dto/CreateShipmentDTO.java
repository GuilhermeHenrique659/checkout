package com.example.checkout.shipment.application.dto;

import java.util.UUID;

public record CreateShipmentDTO() {
    public record Input(UUID orderId) {
    }

    public record Output(UUID shipmentId, UUID routeId, String trackingCode, String status) {
    }
}
