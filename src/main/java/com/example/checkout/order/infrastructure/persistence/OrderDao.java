package com.example.checkout.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderDao extends JpaRepository<OrderModel, UUID> {
    @Query(value = """
                       select
                            o.id AS orderId,
                            o.customer_id AS customerId,
                            p.id AS productId,
                            p.name AS productName,
                            oi.quantity AS quantity,
                            oi.price AS price,
                            o.total as total
                       from orders o
                       join order_items oi on o.id = oi.order_id
                       join products p on oi.product_id = p.id
                       where o.customer_id = :customerId
            """, nativeQuery = true)
    List<OrderDaoDTO.Detail> queryByCustomerId(@Param("customerId") UUID customerId);

    List<OrderModel> id(UUID id);
}

