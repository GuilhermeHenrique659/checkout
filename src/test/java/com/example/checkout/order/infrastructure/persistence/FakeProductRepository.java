package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Product;
import com.example.checkout.order.domain.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeProductRepository implements ProductRepository {
    private Map<UUID, Product> products;

    public FakeProductRepository(List<Product> products) {
        this.products = products.stream().collect(Collectors.toMap(
                Product::id,
                Function.identity()
        ));
    }

    @Override
    public List<Product> valuesByIds(List<UUID> productIds) {
        return productIds.stream()
                .map(productId -> products.get(productId))
                .filter(Objects::nonNull)
                .toList();
    }
}
