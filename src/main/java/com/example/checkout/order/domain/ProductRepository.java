package com.example.checkout.order.domain;

import java.util.List;
import java.util.UUID;

public interface ProductRepository {
    List<Product> valuesByIds(List<UUID> productIds);
}
