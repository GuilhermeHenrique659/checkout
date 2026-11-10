package com.example.checkout.order.domain;

import com.example.checkout.shared.domain.DomainError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class OrderTest {

    @Test
    void shouldCreateANewOrder() {
        //TODO: ADD TEST BUILDER
        var items = List.of(
                new OrderItem(UUID.randomUUID(), 2, 1050),
                new OrderItem(UUID.randomUUID(), 1, 800)
        );

        var customerId = UUID.randomUUID();
        var order = Order.create(customerId, items);

        Assertions.assertEquals(items, order.items());
        Assertions.assertEquals(customerId, order.customerId());
        Assertions.assertEquals("PENDING", order.status());
        Assertions.assertEquals(2900, order.total());
    }

    @Test
    void shouldChangeStatusOfPendingOrder() {
        //TODO: ADD TEST BUILDER
        var items = List.of(
                new OrderItem(UUID.randomUUID(), 2, 1050),
                new OrderItem(UUID.randomUUID(), 1, 800)
        );

        var customerId = UUID.randomUUID();
        var order = Order.create(customerId, items);

        order.changeStatus("PAID");

        Assertions.assertEquals("PAID", order.status());
    }

    @Test
    void shouldTryChangeStatusOfCanceledOrder() {
        //TODO: ADD TEST BUILDER
        var items = List.of(
                new OrderItem(UUID.randomUUID(), 2, 1050),
                new OrderItem(UUID.randomUUID(), 1, 800)
        );

        var customerId = UUID.randomUUID();
        var order = Order.create(customerId, items);

        order.changeStatus("CANCEL");

        Assertions.assertThrowsExactly(DomainError.class, () -> order.changeStatus("DELIVER"));
    }
}