package com.example.checkout.shipment.infrastructure;

import com.example.checkout.shipment.domain.Shipment;
import com.example.checkout.shipment.domain.ShipmentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeShipmentRepository implements ShipmentRepository {
    private final Map<UUID, Shipment> shipments = new HashMap<>();

    @Override
    public Shipment save(Shipment shipment) {
        shipments.put(shipment.id(), shipment);
        return shipment;
    }

    @Override
    public Optional<Shipment> findByOrderId(UUID orderId) {
        return shipments.values().stream()
                .filter(shipment -> shipment.orderId().equals(orderId))
                .findFirst();
    }
}
