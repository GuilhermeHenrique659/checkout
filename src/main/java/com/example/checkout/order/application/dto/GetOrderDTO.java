package com.example.checkout.order.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetOrderDTO() {
    public record Output(UUID id, Integer total, List<Product> products, List<History> history) {
        public record Product(UUID id, Integer price, String name, Integer quantity) {
        }

        public record History(String status, Instant occurredAt) {
        }
    }
}