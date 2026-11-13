package com.example.checkout.order.application;

import com.example.checkout.order.domain.OrderHistory;
import com.example.checkout.order.domain.OrderTimeline;
import java.time.Instant;
import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.application.dto.ProcessPaymentInput;
import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderItem;
import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.order.domain.event.OrderPaid;
import com.example.checkout.order.infrastructure.gateway.PaymentProcessor;
import com.example.checkout.order.infrastructure.gateway.dto.PaymentProcessResult;
import com.example.checkout.order.infrastructure.persistence.FakeOrderRepository;
import com.example.checkout.shared.FakePublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class ProcessPaymentTest {

    @Test
    void ShouldProcessOrderPayment() {
        var items = List.of(
                new OrderItem(UUID.randomUUID(), 2, 1050),
                new OrderItem(UUID.randomUUID(), 1, 800)
        );

        var customerId = UUID.randomUUID();
        var orderToProcess = Order.create(customerId, items);

        var orderRepo = new FakeOrderRepository(List.of(orderToProcess));
        var fakePublisher = new FakePublisher();
        var processPayment = new ProcessPayment(
                orderRepo,
                new PaymentProcessor() {
                    @Override
                    public PaymentProcessResult process(UUID customerId, UUID orderId, Integer total) {
                        return new PaymentProcessResult("SUCCESS");
                    }
                },
                fakePublisher
        );

        processPayment.execute(new ProcessPaymentInput(orderToProcess.id()));

        var orderProcessed = orderRepo.getById(orderToProcess.id());
        Assertions.assertEquals(OrderStatus.PAID, orderProcessed.orElseThrow().status());
        Assertions.assertInstanceOf(OrderPaid.class, fakePublisher.events().getFirst());
    }

    @Test
    void ShouldTryProcessPaymentAndFail() {
        var items = List.of(
                new OrderItem(UUID.randomUUID(), 2, 1050),
                new OrderItem(UUID.randomUUID(), 1, 800)
        );

        var customerId = UUID.randomUUID();
        var orderToProcess = Order.create(customerId, items);

        var orderRepo = new FakeOrderRepository(List.of(orderToProcess));
        var fakePublisher = new FakePublisher();
        var processPayment = new ProcessPayment(
                orderRepo,
                new PaymentProcessor() {
                    @Override
                    public PaymentProcessResult process(UUID customerId, UUID orderId, Integer total) {
                        return new PaymentProcessResult("FAILED");
                    }
                },
                fakePublisher
        );

        processPayment.execute(new ProcessPaymentInput(orderToProcess.id()));

        var orderProcessed = orderRepo.getById(orderToProcess.id());
        Assertions.assertEquals(OrderStatus.CANCEL, orderProcessed.orElseThrow().status());
        Assertions.assertInstanceOf(OrderCanceled.class, fakePublisher.events().getFirst());
    }

    @Test
    void ShouldIgnoreAlreadyProcessedPayment() {
        var items = List.of(new OrderItem(UUID.randomUUID(), 2, 1050));
        var orderToProcess = Order.create(UUID.randomUUID(), items);
        var paidOrder = new Order(orderToProcess.id(), orderToProcess.customerId(), orderToProcess.items(), new OrderTimeline(List.of(new OrderHistory(UUID.randomUUID(), OrderStatus.PAID, Instant.now(), null))), orderToProcess.total());

        var orderRepo = new FakeOrderRepository(List.of(paidOrder));
        var fakePublisher = new FakePublisher();
        var processPayment = new ProcessPayment(
                orderRepo,
                (customerId, orderId, total) -> new PaymentProcessResult("ALREADY_PROCESSED"),
                fakePublisher
        );

        processPayment.execute(new ProcessPaymentInput(paidOrder.id()));

        Assertions.assertEquals(OrderStatus.PAID, orderRepo.getById(paidOrder.id()).orElseThrow().status());
        Assertions.assertTrue(fakePublisher.events().isEmpty());
    }
}