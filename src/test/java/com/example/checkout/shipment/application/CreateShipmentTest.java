package com.example.checkout.shipment.application;

import com.example.checkout.shared.FakePublisher;
import com.example.checkout.shipment.application.dto.CreateShipmentDTO;
import com.example.checkout.shipment.domain.Route;
import com.example.checkout.shipment.domain.ShipmentStatus;
import com.example.checkout.shipment.domain.event.ShipmentCreated;
import com.example.checkout.shipment.infrastructure.FakeRouteRepository;
import com.example.checkout.shipment.infrastructure.FakeShipmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CreateShipmentTest {

    @Test
    void ShouldCreateShipmentForOrder() {
        var route = Route.create("TRUCK-01", "Warehouse A", "Customer address");
        var shipmentRepo = new FakeShipmentRepository();
        var routeRepo = new FakeRouteRepository();
        var publisher = new FakePublisher();
        var createShipment = new CreateShipment(shipmentRepo, routeRepo, orderId -> route, publisher);
        var orderId = UUID.randomUUID();

        var output = createShipment.execute(new CreateShipmentDTO.Input(orderId));

        var shipment = shipmentRepo.findByOrderId(orderId).orElseThrow();
        Assertions.assertEquals(output.shipmentId(), shipment.id());
        Assertions.assertEquals(route.id(), shipment.routeId());
        Assertions.assertEquals(ShipmentStatus.CREATED, shipment.status());
        Assertions.assertFalse(shipment.trackingCode().isBlank());
        Assertions.assertTrue(routeRepo.getById(route.id()).isPresent());

        var event = Assertions.assertInstanceOf(ShipmentCreated.class, publisher.events().getFirst());
        Assertions.assertEquals(shipment.id(), event.shipmentId());
        Assertions.assertEquals(orderId, event.orderId());
    }

    @Test
    void ShouldNotCreateSecondShipmentForSameOrder() {
        var publisher = new FakePublisher();
        var createShipment = new CreateShipment(
                new FakeShipmentRepository(),
                new FakeRouteRepository(),
                orderId -> Route.create("TRUCK-01", "Warehouse A", "Customer address"),
                publisher
        );
        var input = new CreateShipmentDTO.Input(UUID.randomUUID());

        var first = createShipment.execute(input);
        var second = createShipment.execute(input);

        Assertions.assertEquals(first, second);
        Assertions.assertEquals(1, publisher.events().size());
    }
}
