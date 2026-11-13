package com.example.checkout.shipment.infrastructure.persistence;

import com.example.checkout.shipment.domain.Shipment;
import com.example.checkout.shipment.domain.ShipmentRepository;
import com.example.checkout.shipment.domain.ShipmentStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JPAShipmentRepository implements ShipmentRepository {
    private final ShipmentDao shipmentDao;

    public JPAShipmentRepository(ShipmentDao shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

    @Override
    public Shipment save(Shipment shipment) {
        shipmentDao.save(new ShipmentModel(
                shipment.id(),
                shipment.orderId(),
                shipment.routeId(),
                shipment.trackingCode(),
                shipment.status().name()
        ));
        return shipment;
    }

    @Override
    public Optional<Shipment> findByOrderId(UUID orderId) {
        return shipmentDao.findFirstByOrderId(orderId)
                .map(model -> new Shipment(
                        model.getId(),
                        model.getOrderId(),
                        model.getRouteId(),
                        model.getTrackingCode(),
                        ShipmentStatus.valueOf(model.getStatus())
                ));
    }
}
