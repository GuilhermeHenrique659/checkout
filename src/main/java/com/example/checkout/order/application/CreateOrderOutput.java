package com.example.checkout.order.application;

import java.util.UUID;

public record CreateOrderOutput(UUID orderId, String status) {
}
