package com.example.checkout.order.application.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderInput(
        UUID customerId,
        List<OrderItem> items
) {
    public record OrderItem(
            UUID id,
            Integer quantity
    ) {
    }
}
