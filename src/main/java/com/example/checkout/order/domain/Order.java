package com.example.checkout.order.domain;

import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.order.domain.event.OrderPaid;
import com.example.checkout.order.domain.event.OrderPrepared;
import com.example.checkout.shared.domain.DomainError;
import com.example.checkout.shared.domain.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Order {
    private final UUID id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final Integer total;

    private final List<Event> events = new ArrayList<>();

    public Order(UUID id, UUID customerId, List<OrderItem> items, OrderStatus status, Integer total) {
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
                OrderStatus.PENDING,
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
        return status;
    }

    public Integer total() {
        return total;
    }

    public List<Event> events() {
        return List.copyOf(events);
    }

    //TODO: ADD STATE MACHINE
    public void changeStatus(OrderStatus newStatus) {
        switch (status) {
            case PENDING -> {
                if (newStatus == OrderStatus.CANCEL) {
                    events.add(new OrderCanceled(id, status));
                    status = newStatus;
                    break;
                }

                if (newStatus == OrderStatus.PAID) {
                    status = newStatus;
                    events.add(new OrderPaid(id));
                    break;
                }

                throw new DomainError("INVALID_STATUS", "The new Status must be PAID or CANCEL");
            }
            case PAID -> {
                if (newStatus == OrderStatus.CANCEL) {
                    events.add(new OrderCanceled(id, status));
                    status = newStatus;
                    break;
                }

                if (newStatus == OrderStatus.PREPARED) {
                    status = newStatus;
                    events.add(new OrderPrepared(id));
                    break;
                }

                throw new DomainError("INVALID_STATUS", "The new Status must be PREPARED or CANCEL");
            }
            case PREPARED -> {
                if (newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCEL) {
                    status = newStatus;
                    break;
                }
                throw new DomainError("INVALID_STATUS", "The new Status must be SHIPPED or CANCEL");
            }
            case SHIPPED -> {
                if (newStatus == OrderStatus.DELIVER || newStatus == OrderStatus.CANCEL) {
                    status = newStatus;
                    break;
                }
                throw new DomainError("INVALID_STATUS", "The new Status must be DELIVER or CANCEL");
            }
            case DELIVER -> {
                if (newStatus == OrderStatus.CANCEL) {
                    status = newStatus;
                    break;
                }
                throw new DomainError("INVALID_STATUS", "The new Status must be CANCEL");
            }
            case CANCEL -> {
                throw new DomainError("CANCELED_ORDER", "This order was canceled");
            }
        }
    }
}
