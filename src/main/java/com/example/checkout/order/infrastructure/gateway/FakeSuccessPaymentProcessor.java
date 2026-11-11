package com.example.checkout.order.infrastructure.gateway;

import com.example.checkout.order.infrastructure.gateway.dto.PaymentProcessResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FakeSuccessPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessResult process(UUID customerId, UUID orderId, Integer total) {
        return new PaymentProcessResult("SUCCESS");
    }
}
