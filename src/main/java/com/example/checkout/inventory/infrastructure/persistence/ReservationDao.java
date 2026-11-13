package com.example.checkout.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReservationDao extends JpaRepository<ReservationModel, UUID> {
    Optional<ReservationModel> findFirstByReferenceIdOrderByCreatedAtDesc(UUID referenceId);
}
