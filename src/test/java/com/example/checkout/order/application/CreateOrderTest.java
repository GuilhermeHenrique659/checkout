package com.example.checkout.order.application;

import com.example.checkout.order.domain.OrderCreated;
import com.example.checkout.order.domain.Product;
import com.example.checkout.order.infrastructure.persistence.FakeOrderRepository;
import com.example.checkout.order.infrastructure.persistence.FakeProductRepository;
import com.example.checkout.shared.FakeQueue;
import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.domain.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class CreateOrderTest {

    @Test
    void shouldCreateAOrder() {
        var product1Id = UUID.randomUUID();
        var product2Id = UUID.randomUUID();
        var orderRepo = new FakeOrderRepository(List.of());
        var queue = new FakeQueue();
        var received = new ArrayList<Event>();
        queue.subscriber(OrderCreated.class, received::add);

        var createOrder = new CreateOrder(
                orderRepo,
                new FakeProductRepository(List.of(
                        new Product(product1Id, "Product01", 1050),
                        new Product(product2Id, "Product02", 1050)
                )),
                queue
        );

        var output = createOrder.execute(new CreateOrderInput(
                UUID.randomUUID(),
                List.of(
                        new CreateOrderInput.OrderItem(product1Id, 2),
                        new CreateOrderInput.OrderItem(product2Id, 3)
                )
        ));

        Assertions.assertNotNull(output.orderId());
        Assertions.assertEquals(orderRepo.get(output.orderId()).orElseThrow().id(), output.orderId());
        Assertions.assertInstanceOf(OrderCreated.class, received.getFirst());
    }

    @Test
    void shouldTryCreateAOrderWithInvalidProducts() {
        var product1Id = UUID.randomUUID();
        var product2Id = UUID.randomUUID();
        var orderRepo = new FakeOrderRepository(List.of());
        var queue = new FakeQueue();
        var received = new ArrayList<Event>();
        queue.subscriber(OrderCreated.class, received::add);

        var createOrder = new CreateOrder(
                orderRepo,
                new FakeProductRepository(List.of(
                        new Product(product1Id, "Product01", 1050),
                        new Product(product2Id, "Product02", 1050)
                )),
                queue
        );

        Assertions.assertThrows(ApplicationError.class, () -> createOrder.execute(new CreateOrderInput(
                UUID.randomUUID(),
                List.of(
                        new CreateOrderInput.OrderItem(UUID.randomUUID(), 2),
                        new CreateOrderInput.OrderItem(product2Id, 3)
                )
        )));
    }

}