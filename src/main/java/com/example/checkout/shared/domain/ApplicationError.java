package com.example.checkout.shared.domain;

public class ApplicationError extends RuntimeException {
    private final String code;

    public ApplicationError(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
