package com.example.checkout.inventory.infrastructure.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class ReservationModel {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID referenceId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_units", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "unit_id", nullable = false)
    private Set<UUID> unitIds = new HashSet<>();

    protected ReservationModel() {
    }

    public ReservationModel(UUID id, UUID referenceId, String status, Instant createdAt, Set<UUID> unitIds) {
        this.id = id;
        this.referenceId = referenceId;
        this.status = status;
        this.createdAt = createdAt;
        this.unitIds = new HashSet<>(unitIds);
    }

    public UUID getId() {
        return id;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Set<UUID> getUnitIds() {
        return unitIds;
    }
}
