package com.example.checkout.shared.domain;

public class DomainError extends RuntimeException {
    private final String code;

    public DomainError(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
