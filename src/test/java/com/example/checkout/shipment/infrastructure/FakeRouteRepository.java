package com.example.checkout.shipment.infrastructure;

import com.example.checkout.shipment.domain.Route;
import com.example.checkout.shipment.domain.RouteRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeRouteRepository implements RouteRepository {
    private final Map<UUID, Route> routes = new HashMap<>();

    @Override
    public Route save(Route route) {
        routes.put(route.id(), route);
        return route;
    }

    @Override
    public Optional<Route> getById(UUID id) {
        return Optional.ofNullable(routes.get(id));
    }
}
