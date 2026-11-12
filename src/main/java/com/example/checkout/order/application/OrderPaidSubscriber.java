package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.ReserveOrderItemDTO;
import com.example.checkout.order.domain.event.OrderPaid;
import com.example.checkout.shared.infrastructure.queue.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderPaidSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(OrderPaidSubscriber.class);


    public OrderPaidSubscriber(EventBus eventBus, ReserveOrderItem reserveOrderItem) {
        eventBus.subscribe(OrderPaid.class, event -> {
            reserveOrderItem.execute(new ReserveOrderItemDTO.Input(event.orderId()));
            logger.info("ORDER RESERVED: {}", event.orderId());
        });
    }
}
