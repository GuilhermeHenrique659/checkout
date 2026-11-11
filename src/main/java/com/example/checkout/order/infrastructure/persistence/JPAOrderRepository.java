package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

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
}
