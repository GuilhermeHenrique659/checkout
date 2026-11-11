package com.example.checkout.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDao extends JpaRepository<OrderItemModel, OrderItemModelId> {
}
