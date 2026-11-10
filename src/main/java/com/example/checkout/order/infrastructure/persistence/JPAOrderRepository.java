package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JPAOrderRepository implements OrderRepository {
    @Override
    public Order save(Order order) {
        return null;
    }
}
