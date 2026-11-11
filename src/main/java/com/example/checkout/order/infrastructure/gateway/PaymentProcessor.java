package com.example.checkout.order.infrastructure.gateway;

import com.example.checkout.order.infrastructure.gateway.dto.PaymentProcessResult;

import java.util.UUID;

public interface PaymentProcessor {
    PaymentProcessResult process(UUID customerId, UUID orderId, Integer total);
}
