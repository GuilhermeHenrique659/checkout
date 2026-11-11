package com.example.checkout.order.infrastructure.persistence;

import com.example.checkout.order.domain.Product;
import com.example.checkout.order.domain.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JPAProductRepository implements ProductRepository {
    private final ProductDao productDao;

    public JPAProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> valuesByIds(List<UUID> productIds) {
        return productDao.findAllById(productIds)
                .stream()
                .map(data -> new Product(data.getId(), data.getName(), data.getPrice()))
                .toList();
    }
}
