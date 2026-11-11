package com.example.checkout.order.infrastructure.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class OrderItemModelId implements Serializable {

    private UUID orderId;
    private UUID productId;

    public OrderItemModelId() {
    }

    public OrderItemModelId(UUID orderId, UUID productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemModelId that)) return false;

        return Objects.equals(orderId, that.orderId)
                && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
