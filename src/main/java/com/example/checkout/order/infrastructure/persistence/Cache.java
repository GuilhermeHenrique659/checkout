package com.example.checkout.order.infrastructure.persistence;

public interface Cache {
    <T> T get(String key);

    <T> void set(String key, T data);

    boolean has(String key);
}
