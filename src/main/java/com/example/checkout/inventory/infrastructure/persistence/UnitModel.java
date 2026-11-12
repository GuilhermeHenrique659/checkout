package com.example.checkout.inventory.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "units")
public class UnitModel {
    @Id
    private UUID id;

    @Column
    private UUID productId;

    @Column
    private String sku;

    @Column
    private String status;

    protected UnitModel() {
    }

    public UnitModel(UUID id, UUID productId, String sku, String status) {
        this.id = id;
        this.productId = productId;
        this.sku = sku;
        this.status = status;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

