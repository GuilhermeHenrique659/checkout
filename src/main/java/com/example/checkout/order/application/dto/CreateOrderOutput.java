package com.example.checkout.order.application.dto;

import java.util.UUID;

public record CreateOrderOutput(UUID orderId, String status) {
}
