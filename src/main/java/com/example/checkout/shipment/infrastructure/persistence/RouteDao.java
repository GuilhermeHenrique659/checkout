package com.example.checkout.shipment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RouteDao extends JpaRepository<RouteModel, UUID> {
}
