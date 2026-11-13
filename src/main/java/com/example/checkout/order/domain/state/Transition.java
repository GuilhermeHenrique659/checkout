package com.example.checkout.order.domain.state;

import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.shared.domain.Event;

import java.util.List;

public record Transition(OrderStatus to, List<Event> events) {
    static Transition to(OrderStatus status, Event... events) {
        return new Transition(status, List.of(events));
    }
}
