package com.example.checkout.inventory.infrastructure;

import com.example.checkout.inventory.domain.Reservation;
import com.example.checkout.inventory.domain.ReservationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeReservationRepository implements ReservationRepository {
    private final Map<UUID, Reservation> reservations = new HashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findByReferenceId(UUID referenceId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.referenceId().equals(referenceId))
                .findFirst();
    }
}
