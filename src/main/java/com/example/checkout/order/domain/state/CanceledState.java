package com.example.checkout.order.domain.state;

import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.shared.domain.DomainError;

import java.util.UUID;

public final class CanceledState implements OrderState {
    @Override
    public OrderStatus status() {
        return OrderStatus.CANCEL;
    }

    @Override
    public Transition transitionTo(OrderStatus target, UUID orderId) {
        throw new DomainError("CANCELED_ORDER", "This order was canceled");
    }
}
