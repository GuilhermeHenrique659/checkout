package com.example.checkout.order.domain.state;

import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.order.domain.event.OrderPaid;
import com.example.checkout.shared.domain.DomainError;

import java.util.UUID;

public final class PendingState implements OrderState {
    @Override
    public OrderStatus status() {
        return OrderStatus.PENDING;
    }

    @Override
    public Transition transitionTo(OrderStatus target, UUID orderId) {
        return switch (target) {
            case PAID -> Transition.to(target, new OrderPaid(orderId));
            case CANCEL -> Transition.to(target, new OrderCanceled(orderId, status()));
            default -> throw new DomainError("INVALID_STATUS", "The new Status must be PAID or CANCEL");
        };
    }
}
