package com.example.checkout.order.api;

import com.example.checkout.order.api.dto.CreateOrderRequest;
import com.example.checkout.order.api.dto.CreateOrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return new CreateOrderResponse(
                UUID.randomUUID(), "PENDING"
        );
    }

}
