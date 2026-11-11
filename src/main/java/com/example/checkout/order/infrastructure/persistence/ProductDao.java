package com.example.checkout.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductDao extends JpaRepository<ProductModel, UUID> {
}
