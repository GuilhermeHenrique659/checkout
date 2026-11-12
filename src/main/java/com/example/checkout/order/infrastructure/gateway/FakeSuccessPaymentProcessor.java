package com.example.checkout.order.infrastructure.gateway;

import com.example.checkout.order.infrastructure.gateway.dto.PaymentProcessResult;
import com.example.checkout.order.infrastructure.persistence.Cache;
import org.springframework.stereotype.Component;

import java.util.UUID;

record IdempotencyValue(String val, String message) {
}

@Component
public class FakeSuccessPaymentProcessor implements PaymentProcessor {
    private final Cache cache;

    public FakeSuccessPaymentProcessor(Cache cache) {
        this.cache = cache;
    }

    @Override
    public PaymentProcessResult process(UUID customerId, UUID orderId, Integer total) {
        var idempotencyKey = "%s:%s:%s".formatted(customerId.toString(), orderId.toString(), total.toString());
        if (cache.has(idempotencyKey)) return new PaymentProcessResult("ALREADY_PROCESSED");

        cache.set(idempotencyKey, new IdempotencyValue(idempotencyKey, "Process with Success"));

        return new PaymentProcessResult("SUCCESS");
    }
}
