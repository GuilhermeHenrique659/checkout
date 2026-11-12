package com.example.checkout.order.application.dto;

import java.util.List;
import java.util.UUID;

public class CreateOrderDTO {
    public record Input(
            UUID customerId,
            List<OrderItem> items
    ) {
        public record OrderItem(
                UUID id,
                Integer quantity
        ) {
        }
    }

    public record Output(UUID orderId, String status) {
    }
}
