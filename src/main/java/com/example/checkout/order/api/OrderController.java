package com.example.checkout.order.api;

import com.example.checkout.order.api.dto.CreateOrderRequest;
import com.example.checkout.order.api.dto.CreateOrderResponse;
import com.example.checkout.order.api.dto.GetOrderResponse;
import com.example.checkout.order.application.CreateOrder;
import com.example.checkout.order.application.GetOrder;
import com.example.checkout.order.application.dto.CreateOrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrder createOrder;

    private final GetOrder getOrder;

    public OrderController(CreateOrder createOrder, GetOrder getOrder) {
        this.createOrder = createOrder;
        this.getOrder = getOrder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@RequestBody @Valid CreateOrderRequest request) {
        var output = createOrder.execute(new CreateOrderDTO.Input(
                request.customerId(),
                request.items().stream().map(item -> new CreateOrderDTO.Input.OrderItem(
                        item.id(),
                        item.quantity()
                )).toList()
        ));

        return new CreateOrderResponse(output.orderId(), output.status());
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GetOrderResponse getCustomerOrders(@PathVariable UUID customerId) {
        var output = getOrder.execute(customerId);

        return new GetOrderResponse(output);
    }
}
