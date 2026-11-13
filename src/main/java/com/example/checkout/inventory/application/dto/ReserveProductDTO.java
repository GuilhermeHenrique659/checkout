package com.example.checkout.inventory.application.dto;

import java.util.List;
import java.util.UUID;

public record ReserveProductDTO() {
    public record ProductItem(UUID productId, Integer quantity) {
    }

    public record Input(UUID referenceId, List<ProductItem> productItems) {
    }

    public record Output(UUID reservationId, List<ProductItem> productItems) {
    }
}
