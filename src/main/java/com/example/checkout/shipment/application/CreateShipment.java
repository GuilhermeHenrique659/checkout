package com.example.checkout.shipment.application;

import com.example.checkout.shared.infrastructure.queue.Publisher;
import com.example.checkout.shipment.application.dto.CreateShipmentDTO;
import com.example.checkout.shipment.domain.RouteRepository;
import com.example.checkout.shipment.domain.Shipment;
import com.example.checkout.shipment.domain.ShipmentRepository;
import com.example.checkout.shipment.infrastructure.gateway.RoutePlanner;
import org.springframework.stereotype.Service;

@Service
public class CreateShipment {
    private final ShipmentRepository shipmentRepository;
    private final RouteRepository routeRepository;
    private final RoutePlanner routePlanner;
    private final Publisher publisher;

    public CreateShipment(ShipmentRepository shipmentRepository, RouteRepository routeRepository, RoutePlanner routePlanner, Publisher publisher) {
        this.shipmentRepository = shipmentRepository;
        this.routeRepository = routeRepository;
        this.routePlanner = routePlanner;
        this.publisher = publisher;
    }

    public CreateShipmentDTO.Output execute(CreateShipmentDTO.Input input) {
        // Duplicate delivery of OrderPrepared: the order already has a shipment, so return it untouched.
        var existing = shipmentRepository.findByOrderId(input.orderId());
        if (existing.isPresent()) return toOutput(existing.get());

        var route = routePlanner.plan(input.orderId());
        var shipment = Shipment.create(input.orderId(), route);

        routeRepository.save(route);
        shipmentRepository.save(shipment);
        shipment.events().forEach(publisher::publish);

        return toOutput(shipment);
    }

    private CreateShipmentDTO.Output toOutput(Shipment shipment) {
        return new CreateShipmentDTO.Output(shipment.id(), shipment.routeId(), shipment.trackingCode(), shipment.status().name());
    }
}
