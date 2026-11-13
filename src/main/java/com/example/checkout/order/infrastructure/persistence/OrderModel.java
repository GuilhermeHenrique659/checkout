package com.example.checkout.order.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderModel {
    @Id
    private UUID id;

    @Column
    private UUID customerId;

    @Column
    private Integer total;

    protected OrderModel() {
    }

    public OrderModel(UUID id, UUID customerId, Integer total) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}