package com.example.checkout.shipment.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "routes")
public class RouteModel {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String vehicle;

    @Column(name = "start_location", nullable = false)
    private String start;

    @Column(name = "end_location", nullable = false)
    private String end;

    protected RouteModel() {
    }

    public RouteModel(UUID id, String vehicle, String start, String end) {
        this.id = id;
        this.vehicle = vehicle;
        this.start = start;
        this.end = end;
    }

    public UUID getId() {
        return id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
