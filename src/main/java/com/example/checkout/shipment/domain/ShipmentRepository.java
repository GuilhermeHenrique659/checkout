package com.example.checkout.shipment.domain;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository {
    Shipment save(Shipment shipment);

    Optional<Shipment> findByOrderId(UUID orderId);
}
