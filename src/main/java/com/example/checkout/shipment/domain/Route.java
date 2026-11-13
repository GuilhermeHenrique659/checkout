package com.example.checkout.shipment.domain;

import java.util.UUID;

public class Route {
    private final UUID id;
    private final String vehicle;
    private final String start;
    private final String end;

    public Route(UUID id, String vehicle, String start, String end) {
        this.id = id;
        this.vehicle = vehicle;
        this.start = start;
        this.end = end;
    }

    public static Route create(String vehicle, String start, String end) {
        return new Route(UUID.randomUUID(), vehicle, start, end);
    }

    public UUID id() {
        return id;
    }

    public String vehicle() {
        return vehicle;
    }

    public String start() {
        return start;
    }

    public String end() {
        return end;
    }
}
