package com.example.checkout.inventory.domain;

import com.example.checkout.shared.domain.DomainError;

import java.util.UUID;

public class Unit {
    private final UUID id;

    private final UUID productId;

    private final String sku;

    private String status;

    public Unit(String status, String sku, UUID productId, UUID id) {
        this.status = status;
        this.sku = sku;
        this.productId = productId;
        this.id = id;
    }

    public static Unit create(UUID productId, String sku) {
        return new Unit("AVAILABLE", sku, productId, UUID.randomUUID());
    }

    public UUID id() {
        return id;
    }

    public UUID productId() {
        return productId;
    }

    public String sku() {
        return sku;
    }

    public String status() {
        return status;
    }

    public void reserve() {
        if (!status.equals("AVAILABLE")) throw new DomainError("UNIT_UNAVAILABLE", "Unit not available");

        status = "RESERVED";
    }
}
