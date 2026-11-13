package com.example.checkout.shipment.domain;

import java.util.Optional;
import java.util.UUID;

public interface RouteRepository {
    Route save(Route route);

    Optional<Route> getById(UUID id);
}
