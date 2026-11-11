package com.example.checkout.order.infrastructure.persistence;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order_items")
@IdClass(OrderItemModelId.class)
public class OrderItemModel {

    @Id
    private UUID orderId;

    @Id
    private UUID productId;

    @Column
    private Integer quantity;

    @Column
    private Integer price;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    protected OrderItemModel() {
    }

    public OrderItemModel(UUID orderId, UUID productId, Integer quantity, Integer price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}
