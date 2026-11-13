package com.example.checkout.shipment.infrastructure.gateway;

import com.example.checkout.shipment.domain.Route;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Study stub: always plans the same truck route from the warehouse. */
@Component
public class FakeRoutePlanner implements RoutePlanner {
    @Override
    public Route plan(UUID orderId) {
        return Route.create("TRUCK-01", "Main warehouse", "Customer address");
    }
}
