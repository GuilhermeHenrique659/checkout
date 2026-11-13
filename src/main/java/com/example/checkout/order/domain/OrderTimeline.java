package com.example.checkout.order.domain;

import com.example.checkout.shared.domain.DomainError;

import java.util.ArrayList;
import java.util.List;

/**
 * Append-only, ordered history of an order's status changes, linked from the first to the current one.
 */
public class OrderTimeline {
    private final List<OrderHistory> histories;

    public OrderTimeline(List<OrderHistory> histories) {
        validate(histories);
        this.histories = new ArrayList<>(histories);
    }

    static OrderTimeline start(OrderStatus status) {
        return new OrderTimeline(List.of(OrderHistory.create(status)));
    }

    private static void validate(List<OrderHistory> histories) {
        if (histories == null || histories.isEmpty()) {
            throw new DomainError("INVALID_STATUS_HISTORY", "Order must have at least one status");
        }
        for (int i = 0; i < histories.size() - 1; i++) {
            var pointer = histories.get(i).next();
            if (pointer.isEmpty() || !pointer.get().equals(histories.get(i + 1).id())) {
                throw new DomainError("INVALID_STATUS_HISTORY", "Status history is not a valid linked list");
            }
        }
        if (!histories.getLast().isCurrent()) {
            throw new DomainError("INVALID_STATUS_HISTORY", "Last status must not point to another status");
        }
    }

    public OrderHistory current() {
        return histories.getLast();
    }

    public OrderStatus currentStatus() {
        return current().status();
    }

    void append(OrderStatus status) {
        var history = OrderHistory.create(status);
        current().linkTo(history);
        histories.add(history);
    }

    /** Read-only view, ordered from first to current. */
    public List<OrderHistory> histories() {
        return List.copyOf(histories);
    }
}
