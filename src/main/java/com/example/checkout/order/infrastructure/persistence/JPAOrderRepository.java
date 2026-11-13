package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderItem;
import com.example.checkout.order.domain.OrderRepository;
import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.domain.OrderHistory;
import com.example.checkout.order.domain.OrderTimeline;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JPAOrderRepository implements OrderRepository {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderHistoryDao orderHistoryDao;

    public JPAOrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, OrderHistoryDao orderHistoryDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderHistoryDao = orderHistoryDao;
    }

    @Override
    public Order save(Order order) {
        orderDao.save(new OrderModel(order.id(), order.customerId(), order.total()));

        var orderItems = order.items()
                .stream()
                .map(item -> new OrderItemModel(order.id(), item.id(), item.quantity(), item.price()))
                .toList();

        orderItemDao.saveAll(orderItems);

        // Append-only: new entries are inserted; the only update is the previous tail's next pointer.
        var statuses = order.histories()
                .stream()
                .map(change -> new OrderHistoryModel(
                        change.id(),
                        order.id(),
                        change.status().name(),
                        change.occurredAt(),
                        change.next().orElse(null)
                ))
                .toList();

        orderHistoryDao.saveAll(statuses);

        return order;
    }

    @Override
    public Optional<Order> getById(UUID id) {
        var orderModel = orderDao.findById(id);

        if (orderModel.isEmpty()) return Optional.empty();

        var orderItems = orderItemDao.findAllByOrderId(id).stream()
                .map(data -> new OrderItem(data.getProductId(), data.getQuantity(), data.getPrice()))
                .toList();

        return Optional.of(new Order(
                orderModel.get().getId(),
                orderModel.get().getCustomerId(),
                orderItems,
                new OrderTimeline(loadStatusHistory(id)),
                orderModel.get().getTotal()
        ));
    }

    private List<OrderHistory> loadStatusHistory(UUID orderId) {
        return OrderHistoryChain.follow(orderId, orderHistoryDao.findAllByOrderId(orderId))
                .stream()
                .map(model -> new OrderHistory(
                        model.getId(),
                        OrderStatus.valueOf(model.getStatus()),
                        model.getOccurredAt(),
                        model.getNextId()
                ))
                .toList();
    }
}
