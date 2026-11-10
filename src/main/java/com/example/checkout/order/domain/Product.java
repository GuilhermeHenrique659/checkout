package com.example.checkout.order.domain;

import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final Integer price;

    public Product(UUID id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Integer price() {
        return price;
    }
}
