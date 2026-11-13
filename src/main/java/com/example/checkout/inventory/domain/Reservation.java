package com.example.checkout.inventory.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Reservation {
    private final UUID id;

    private final UUID referenceId;

    private ReservationStatus status;

    private final Instant createdAt;

    private final List<UUID> unitIds;

    public Reservation(UUID id, UUID referenceId, ReservationStatus status, Instant createdAt, List<UUID> unitIds) {
        this.id = id;
        this.referenceId = referenceId;
        this.status = status;
        this.createdAt = createdAt;
        this.unitIds = List.copyOf(unitIds);
    }

    public static Reservation create(UUID referenceId, List<Unit> units) {
        units.forEach(Unit::reserve);

        return new Reservation(
                UUID.randomUUID(),
                referenceId,
                ReservationStatus.ACTIVE,
                Instant.now(),
                units.stream().map(Unit::id).toList()
        );
    }

    public UUID id() {
        return id;
    }

    public UUID referenceId() {
        return referenceId;
    }

    public ReservationStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public List<UUID> unitIds() {
        return unitIds;
    }
}
