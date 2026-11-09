package com.example.checkout.order.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotEmpty List<@Valid OrderItem> items
) {
    public record OrderItem(
            @NotNull UUID id,
            @NotNull @Positive Integer quantity
    ) {
    }
}