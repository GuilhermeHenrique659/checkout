package com.example.checkout.order.domain.state;

import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.shared.domain.DomainError;

import java.util.UUID;

public final class DeliveredState implements OrderState {
    @Override
    public OrderStatus status() {
        return OrderStatus.DELIVER;
    }

    @Override
    public Transition transitionTo(OrderStatus target, UUID orderId) {
        return switch (target) {
            case CANCEL -> Transition.to(target, new OrderCanceled(orderId, status()));
            default -> throw new DomainError("INVALID_STATUS", "The new Status must be CANCEL");
        };
    }
}
