package com.example.checkout.order.api.dto;

import java.util.UUID;

public record CreateOrderResponse(UUID id, String status) {
}
