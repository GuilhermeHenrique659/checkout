package com.example.checkout.order.application.dto;

import java.util.UUID;

public record ReserveOrderItemDTO() {
    public record Input(UUID orderId) {
    }

    public record Output(UUID orderId) {
    }
}
