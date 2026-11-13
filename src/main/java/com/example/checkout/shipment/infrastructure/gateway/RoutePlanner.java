package com.example.checkout.shipment.infrastructure.gateway;

import com.example.checkout.shipment.domain.Route;

import java.util.UUID;

public interface RoutePlanner {
    Route plan(UUID orderId);
}
