package com.example.checkout.order.application;

import com.example.checkout.order.domain.*;
import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.queue.Queue;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class CreateOrder {

    private final Queue queue;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    public CreateOrder(OrderRepository orderRepo, ProductRepository productRepo, Queue queue) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.queue = queue;
    }

    public CreateOrderOutput execute(CreateOrderInput input) {
        var products = productRepo
                .valuesByIds(input.items().stream().map(CreateOrderInput.OrderItem::id).toList())
                .stream()
                .collect(
                        Collectors.toMap(
                                Product::id,
                                Function.identity()
                        )
                );

        var missingProducts = input.items().stream().filter(item -> !products.containsKey(item.id()));

        if (missingProducts.findAny().isPresent())
            throw new ApplicationError("MISSING_PRODUCTS", "Some products not found");

        var orderItems = input.items().stream()
                .filter(item -> products.containsKey(item.id()))
                .map(item -> new OrderItem(
                        products.get(item.id()).id(),
                        item.quantity(),
                        products.get(item.id()).price()
                ))
                .toList();

        var order = Order.create(input.customerId(), orderItems);

        orderRepo.save(order);

        queue.publish(new OrderCreated(order.id()));

        return new CreateOrderOutput(order.id(), order.status());
    }
}
