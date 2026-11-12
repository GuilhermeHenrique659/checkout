package com.example.checkout.order.application.dto;

import java.util.List;
import java.util.UUID;

public record ReserveItemInventoryServiceDTO() {
    public record Input(UUID orderId, List<Item> items) {
        public record Item(UUID itemId, Integer quantity) {
        }
    }

    public record Output(UUID itemId) {
    }
}
