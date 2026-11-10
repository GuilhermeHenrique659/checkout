package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Order;
import com.example.checkout.order.domain.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeOrderRepository implements OrderRepository {
    private Map<UUID, Order> orders;

    public FakeOrderRepository(List<Order> orders) {
        this.orders = orders.stream().collect(Collectors.toMap(
                Order::id,
                Function.identity()
        ));
    }

    public Optional<Order> get(UUID id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public Order save(Order order) {
        return orders.put(order.id(), order);
    }
}
