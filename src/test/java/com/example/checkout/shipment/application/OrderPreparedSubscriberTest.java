package com.example.checkout.shipment.application;

import com.example.checkout.order.domain.event.OrderPrepared;
import com.example.checkout.shared.FakePublisher;
import com.example.checkout.shipment.domain.Route;
import com.example.checkout.shipment.infrastructure.FakeRouteRepository;
import com.example.checkout.shipment.infrastructure.FakeShipmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class OrderPreparedSubscriberTest {

    @Test
    void ShouldCreateShipmentWhenOrderIsPrepared() {
        var shipmentRepo = new FakeShipmentRepository();
        var eventBus = new FakePublisher();
        var createShipment = new CreateShipment(
                shipmentRepo,
                new FakeRouteRepository(),
                orderId -> Route.create("TRUCK-01", "Warehouse A", "Customer address"),
                eventBus
        );
        new OrderPreparedSubscriber(eventBus, createShipment);
        var orderId = UUID.randomUUID();

        eventBus.publish(new OrderPrepared(orderId));

        Assertions.assertTrue(shipmentRepo.findByOrderId(orderId).isPresent());
    }
}
