package com.example.checkout.order.application;

import com.example.checkout.order.domain.OrderCreated;
import com.example.checkout.shared.queue.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedSubscriber {
    public OrderCreatedSubscriber(@Qualifier("springQueue") Queue queue) {

        queue.subscriber(OrderCreated.class, (event -> {
            IO.println("ORDER CREATED: " + event.orderId());
        }));
    }
}
