package com.example.checkout.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemDao extends JpaRepository<OrderItemModel, OrderItemModelId> {
    List<OrderItemModel> findAllByOrderId(UUID orderId);
}
