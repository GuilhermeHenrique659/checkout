package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.ReserveOrderItemDTO;
import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderHistory;
import com.example.checkout.order.domain.OrderItem;
import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.domain.OrderTimeline;
import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.order.domain.event.OrderPrepared;
import com.example.checkout.order.infrastructure.persistence.FakeOrderRepository;
import com.example.checkout.shared.FakePublisher;
import com.example.checkout.shared.domain.ApplicationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class ReserveOrderItemTest {

    private Order paidOrder() {
        var created = Order.create(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 2, 1050)));
        return new Order(created.id(), created.customerId(), created.items(),
                new OrderTimeline(List.of(new OrderHistory(UUID.randomUUID(), OrderStatus.PAID, Instant.now(), null))),
                created.total());
    }

    @Test
    void ShouldPrepareOrderWhenItemsAreReserved() {
        var order = paidOrder();
        var orderRepo = new FakeOrderRepository(List.of(order));
        var publisher = new FakePublisher();
        var reserveOrderItem = new ReserveOrderItem(orderRepo, publisher, input -> List.of());

        reserveOrderItem.execute(new ReserveOrderItemDTO.Input(order.id()));

        Assertions.assertEquals(OrderStatus.PREPARED, orderRepo.getById(order.id()).orElseThrow().status());
        Assertions.assertEquals(1, publisher.events().size());
        Assertions.assertInstanceOf(OrderPrepared.class, publisher.events().getFirst());
    }

    @Test
    void ShouldCancelWithoutPreparingWhenReservationFails() {
        var order = paidOrder();
        var orderRepo = new FakeOrderRepository(List.of(order));
        var publisher = new FakePublisher();
        var reserveOrderItem = new ReserveOrderItem(orderRepo, publisher, input -> {
            throw new ApplicationError("PRODUCT_UNAVAILABLE", "Product quantity is not available");
        });

        reserveOrderItem.execute(new ReserveOrderItemDTO.Input(order.id()));

        Assertions.assertEquals(OrderStatus.CANCEL, orderRepo.getById(order.id()).orElseThrow().status());
        Assertions.assertEquals(1, publisher.events().size());
        Assertions.assertInstanceOf(OrderCanceled.class, publisher.events().getFirst());
    }
}
