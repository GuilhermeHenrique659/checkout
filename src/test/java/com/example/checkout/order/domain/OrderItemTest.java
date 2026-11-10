package com.example.checkout.order.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderItemTest {

    @Test
    void calculateOderItemTotal() {
        var price = new OrderItem(UUID.randomUUID(), 2, 1050);

        Assertions.assertEquals(2100, price.calculateOderItemTotal());
    }
}