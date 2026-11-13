package com.example.checkout.shipment.infrastructure.persistence;

import com.example.checkout.shipment.domain.Route;
import com.example.checkout.shipment.domain.RouteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JPARouteRepository implements RouteRepository {
    private final RouteDao routeDao;

    public JPARouteRepository(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    @Override
    public Route save(Route route) {
        routeDao.save(new RouteModel(route.id(), route.vehicle(), route.start(), route.end()));
        return route;
    }

    @Override
    public Optional<Route> getById(UUID id) {
        return routeDao.findById(id)
                .map(model -> new Route(model.getId(), model.getVehicle(), model.getStart(), model.getEnd()));
    }
}
