package com.example.checkout.order.api;

import com.example.checkout.order.api.dto.CreateOrderRequest;
import com.example.checkout.order.api.dto.CreateOrderResponse;
import com.example.checkout.order.application.CreateOrder;
import com.example.checkout.order.application.dto.CreateOrderInput;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrder createOrder;

    public OrderController(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@RequestBody @Valid CreateOrderRequest request) {
        var output = createOrder.execute(new CreateOrderInput(
                request.customerId(),
                request.items().stream().map(item -> new CreateOrderInput.OrderItem(
                        item.id(),
                        item.quantity()
                )).toList()
        ));

        return new CreateOrderResponse(output.orderId(), output.status());
    }

}
