package com.example.checkout.order.domain;

import com.example.checkout.order.domain.state.OrderState;
import com.example.checkout.shared.domain.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Order {
    private final UUID id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private final OrderTimeline timeline;
    private final Integer total;

    private final List<Event> events = new ArrayList<>();

    public Order(UUID id, UUID customerId, List<OrderItem> items, OrderTimeline timeline, Integer total) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.timeline = timeline;
        this.total = total;
    }

    public static Order create(UUID customerId, List<OrderItem> items) {
        return new Order(
                UUID.randomUUID(),
                customerId,
                items,
                OrderTimeline.start(OrderStatus.PENDING),
                items.stream().reduce(0, (acc, item) -> acc + item.calculateOderItemTotal(), Integer::sum)
        );
    }

    public UUID id() {
        return id;
    }

    public UUID customerId() {
        return customerId;
    }

    public List<OrderItem> items() {
        return List.copyOf(items);
    }

    public OrderStatus status() {
        return timeline.currentStatus();
    }

    /** Read-only, append-only status history, ordered from first to current. */
    public List<OrderHistory> histories() {
        return timeline.histories();
    }

    public Integer total() {
        return total;
    }

    public List<Event> events() {
        return List.copyOf(events);
    }

    public void changeStatus(OrderStatus newStatus) {
        var transition = OrderState.of(status()).transitionTo(newStatus, id);
        timeline.append(transition.to());
        events.addAll(transition.events());
    }
}
