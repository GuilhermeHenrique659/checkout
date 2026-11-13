package com.example.checkout.shipment.application;

import com.example.checkout.order.domain.event.OrderPrepared;
import com.example.checkout.shared.infrastructure.queue.EventBus;
import com.example.checkout.shipment.application.dto.CreateShipmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderPreparedSubscriber {
    private static final Logger logger = LoggerFactory.getLogger(OrderPreparedSubscriber.class);

    public OrderPreparedSubscriber(EventBus eventBus, CreateShipment createShipment) {
        eventBus.subscribe(OrderPrepared.class, event -> {
            var output = createShipment.execute(new CreateShipmentDTO.Input(event.orderId()));
            logger.info("SHIPMENT CREATED: {} for order {}", output.trackingCode(), event.orderId());
        });
    }
}
