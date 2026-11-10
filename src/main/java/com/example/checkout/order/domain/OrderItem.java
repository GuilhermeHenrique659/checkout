package com.example.checkout.order.domain;

import java.util.UUID;

public class OrderItem {
    private final UUID id;
    private final Integer quantity;
    private final Integer price;

    public OrderItem(UUID id, Integer quantity, Integer price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID id() {
        return id;
    }

    public Integer quantity() {
        return quantity;
    }

    public Integer price() {
        return price;
    }

    public Integer calculateOderItemTotal() {
        return quantity * price;
    }
}
