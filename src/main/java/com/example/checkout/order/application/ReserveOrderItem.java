package com.example.checkout.order.application;

import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.application.dto.ReserveItemInventoryServiceDTO;
import com.example.checkout.order.application.dto.ReserveOrderItemDTO;
import com.example.checkout.order.domain.OrderRepository;
import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.domain.DomainError;
import com.example.checkout.shared.infrastructure.queue.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReserveOrderItem {
    private final OrderRepository orderRepo;
    private final Publisher publisher;
    private final OrderInventoryService orderInventoryService;
    private final Logger logger = LoggerFactory.getLogger(ReserveOrderItem.class);

    public ReserveOrderItem(OrderRepository orderRepo, Publisher publisher, OrderInventoryService orderInventoryService) {
        this.orderRepo = orderRepo;
        this.publisher = publisher;
        this.orderInventoryService = orderInventoryService;
    }

    public ReserveOrderItemDTO.Output execute(ReserveOrderItemDTO.Input input) {
        var order = orderRepo.getById(input.orderId())
                .orElseThrow(() -> new ApplicationError("ORDER_NOT_FOUND", "Order Not Found"));

        var itemsToReserve = order.items().stream()
                .map(item -> new ReserveItemInventoryServiceDTO.Input.Item(
                        item.id(),
                        item.quantity()
                ))
                .toList();

        try {
            orderInventoryService.reserveItems(new ReserveItemInventoryServiceDTO.Input(
                    order.id(),
                    itemsToReserve
            ));

            order.changeStatus(OrderStatus.PREPARED);
            orderRepo.save(order);
        } catch (RuntimeException e) {
            if (e instanceof ApplicationError || e instanceof DomainError) {
                order.changeStatus(OrderStatus.CANCEL);
                logger.info("Order cancelled. Reason: {}", e.getMessage());
                orderRepo.save(order);
            } else {
                throw e;
            }
        }

        order.events().forEach(publisher::publish);

        return new ReserveOrderItemDTO.Output(order.id());
    }
}
