package com.example.checkout.order.domain.state;

import com.example.checkout.order.domain.OrderStatus;

import java.util.UUID;

/**
 * State pattern: each order status knows which transitions it allows and which events they raise.
 */
public sealed interface OrderState
        permits PendingState, PaidState, PreparedState, ShippedState, DeliveredState, CanceledState {

    OrderStatus status();

    Transition transitionTo(OrderStatus target, UUID orderId);

    static OrderState of(OrderStatus status) {
        return switch (status) {
            case PENDING -> new PendingState();
            case PAID -> new PaidState();
            case PREPARED -> new PreparedState();
            case SHIPPED -> new ShippedState();
            case DELIVER -> new DeliveredState();
            case CANCEL -> new CanceledState();
        };
    }
}
