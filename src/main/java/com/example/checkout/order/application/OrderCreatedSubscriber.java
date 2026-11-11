package com.example.checkout.order.application;

import com.example.checkout.order.domain.OrderCreated;
import com.example.checkout.shared.infrastructure.queue.EventBus;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedSubscriber {
    public OrderCreatedSubscriber(EventBus eventBus) {
        eventBus.subscribe(OrderCreated.class, (event -> {
            IO.println("ORDER CREATED: " + event.orderId());
        }));
    }
}
