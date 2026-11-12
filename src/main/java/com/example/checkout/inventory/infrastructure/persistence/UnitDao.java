package com.example.checkout.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UnitDao extends JpaRepository<UnitModel, UUID> {

    @Query(value = """
            SELECT u.*
            FROM unnest(:productIds, :quantities) AS request(product_id, qtd)
            CROSS JOIN LATERAL (
                SELECT *
                FROM units
                WHERE product_id = request.product_id
                  AND status = :status
                ORDER BY id
                LIMIT request.qtd
            ) u
            """, nativeQuery = true)
    List<UnitModel> getAvailable(
            @Param("productIds") UUID[] productIds,
            @Param("quantities") Integer[] quantities,
            @Param("status") String status
    );
}
