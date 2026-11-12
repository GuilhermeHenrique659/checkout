package com.example.checkout.order.infrastructure.persistence;

import org.springframework.stereotype.Component;

@Component
public class MapCache implements Cache {
    @Override
    public <T> void set(String key, T data) {

    }

    @Override
    public boolean has(String key) {
        return false;
    }

    @Override
    public <T> T get(String key) {
        return null;
    }
}
