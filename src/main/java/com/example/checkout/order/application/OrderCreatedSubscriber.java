package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.ProcessPaymentInput;
import com.example.checkout.order.domain.event.OrderCreated;
import com.example.checkout.shared.infrastructure.queue.EventBus;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedSubscriber {
    public OrderCreatedSubscriber(EventBus eventBus, ProcessPayment processPayment) {
        eventBus.subscribe(OrderCreated.class, (event -> {
            processPayment.execute(new ProcessPaymentInput(event.orderId()));
            IO.println("ORDER PAYMENT PROCESSED: " + event.orderId());
        }));
    }
}
