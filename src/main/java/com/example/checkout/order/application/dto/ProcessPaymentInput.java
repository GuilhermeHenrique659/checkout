package com.example.checkout.order.application.dto;

import java.util.UUID;

public record ProcessPaymentInput(UUID orderId) {
}
