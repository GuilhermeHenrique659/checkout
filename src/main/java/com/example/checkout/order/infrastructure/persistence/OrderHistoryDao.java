package com.example.checkout.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OrderHistoryDao extends JpaRepository<OrderHistoryModel, UUID> {
    List<OrderHistoryModel> findAllByOrderId(UUID orderId);

    List<OrderHistoryModel> findAllByOrderIdIn(Collection<UUID> orderIds);
}
