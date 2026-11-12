package com.example.checkout.inventory.application.dto;

import java.util.List;
import java.util.UUID;

public record ReserveProductDTO() {
    public record ProductItem(UUID productId, Integer quantity) {
    }

    public record Input(List<ProductItem> productItems) {
    }

    public record Output(List<ProductItem> productItems) {
    }
}
