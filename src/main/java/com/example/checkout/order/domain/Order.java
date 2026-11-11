package com.example.checkout.order.domain;

import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.order.domain.event.OrderPaid;
import com.example.checkout.shared.domain.DomainError;
import com.example.checkout.shared.domain.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Order {
    private final UUID id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private String status;
    private final Integer total;

    private final List<Event> events = new ArrayList<>();

    public Order(UUID id, UUID customerId, List<OrderItem> items, String status, Integer total) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.total = total;
    }

    public static Order create(UUID customerId, List<OrderItem> items) {
        return new Order(
                UUID.randomUUID(),
                customerId,
                items,
                "PENDING",
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

    public String status() {
        return status;
    }

    public Integer total() {
        return total;
    }

    public List<Event> events() {
        return List.copyOf(events);
    }

    //TODO: ADD STATE MACHINE
    public void changeStatus(String newStatus) {
        switch (status) {
            case "PENDING" -> {
                if (newStatus.equals("CANCEL")) {
                    status = newStatus;
                    events.add(new OrderCanceled(id));
                    break;
                }

                if (newStatus.equals("PAID")) {
                    status = newStatus;
                    events.add(new OrderPaid(id));
                    break;
                }

                throw new DomainError("INVALID_STATUS", "The new Status must be PAID or CANCEL");
            }
            case "PAID" -> {
                if (newStatus.equals("PREPARE") || newStatus.equals("CANCEL")) {
                    status = newStatus;
                    break;
                }

                throw new DomainError("INVALID_STATUS", "The new Status must be PREPARE or CANCEL");
            }
            case "PREPARE" -> {
                if (newStatus.equals("SHIPPED") || newStatus.equals("CANCEL")) {
                    status = newStatus;
                    break;
                }
                throw new DomainError("INVALID_STATUS", "The new Status must be SHIPPED or CANCEL");
            }
            case "SHIPPED" -> {
                if (newStatus.equals("DELIVER") || newStatus.equals("CANCEL")) {
                    status = newStatus;
                    break;
                }
                throw new DomainError("INVALID_STATUS", "The new Status must be DELIVER or CANCEL");
            }
            case "DELIVER" -> {
                if (newStatus.equals("CANCEL")) {
                    status = newStatus;
                    break;
                }
                throw new DomainError("INVALID_STATUS", "The new Status must be CANCEL");
            }
            case "CANCEL" -> {
                throw new DomainError("CANCELED_ORDER", "This order was canceled");
            }
        }
    }
}
