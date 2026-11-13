package com.example.checkout.shipment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentDao extends JpaRepository<ShipmentModel, UUID> {
    Optional<ShipmentModel> findFirstByOrderId(UUID orderId);
}
