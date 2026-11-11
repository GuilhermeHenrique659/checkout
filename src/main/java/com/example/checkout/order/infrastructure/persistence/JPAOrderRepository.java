package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderItem;
import com.example.checkout.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JPAOrderRepository implements OrderRepository {
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public JPAOrderRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public Order save(Order order) {
        orderDao.save(new OrderModel(order.id(), order.status(), order.customerId(), order.total()));

        var orderItems = order.items()
                .stream()
                .map(item -> new OrderItemModel(order.id(), item.id(), item.quantity(), item.price()))
                .toList();

        orderItemDao.saveAll((orderItems));

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
                orderModel.get().getStatus(),
                orderModel.get().getTotal()
        ));
    }
}
