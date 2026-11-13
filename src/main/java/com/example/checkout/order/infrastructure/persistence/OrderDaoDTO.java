package com.example.checkout.order.infrastructure.persistence;

import java.util.UUID;

public record OrderDaoDTO() {
    public record Detail(UUID orderId, UUID customerId, UUID productId, String productName, Integer quantity,
                         Integer price, Integer total) {
    }
}

