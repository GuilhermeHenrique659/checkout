package com.example.checkout.inventory.domain;

import java.util.UUID;

public class Product {
    private final UUID id;

    private String name;

    public Product(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }
}
