package com.example.checkout.inventory.domain;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findByReferenceId(UUID referenceId);
}
