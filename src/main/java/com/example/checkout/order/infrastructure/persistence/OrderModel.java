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
    private String status;

    @Column
    private UUID customerId;

    @Column
    private Integer total;

    protected OrderModel() {
    }

    public OrderModel(UUID id, String status, UUID customerId, Integer total) {
        this.id = id;
        this.status = status;
        this.customerId = customerId;
        this.total = total;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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