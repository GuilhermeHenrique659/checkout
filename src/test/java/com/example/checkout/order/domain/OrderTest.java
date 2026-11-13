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
        Assertions.assertEquals(OrderStatus.PENDING, order.status());
        Assertions.assertEquals(2900, order.total());
    }

    @Test
    void shouldAppendStatusHistoryAsLinkedList() {
        var order = Order.create(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 1, 100)));

        order.changeStatus(OrderStatus.PAID);
        order.changeStatus(OrderStatus.PREPARED);

        var history = order.histories();
        Assertions.assertEquals(
                List.of(OrderStatus.PENDING, OrderStatus.PAID, OrderStatus.PREPARED),
                history.stream().map(OrderHistory::status).toList()
        );
        Assertions.assertEquals(history.get(1).id(), history.get(0).next().orElseThrow());
        Assertions.assertEquals(history.get(2).id(), history.get(1).next().orElseThrow());
        Assertions.assertTrue(history.get(2).isCurrent());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> history.add(history.getFirst()));
    }

    @Test
    void shouldRejectBrokenStatusHistory() {
        var first = new OrderHistory(UUID.randomUUID(), OrderStatus.PENDING, java.time.Instant.now(), null);
        var second = new OrderHistory(UUID.randomUUID(), OrderStatus.PAID, java.time.Instant.now(), null);

        Assertions.assertThrowsExactly(DomainError.class,
                () -> new OrderTimeline(List.of(first, second)));
    }

}
