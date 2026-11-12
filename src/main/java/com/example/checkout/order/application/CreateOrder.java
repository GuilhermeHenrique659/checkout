package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.CreateOrderDTO;
import com.example.checkout.order.domain.*;
import com.example.checkout.order.domain.event.OrderCreated;
import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.infrastructure.queue.Publisher;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class CreateOrder {

    private final Publisher publisher;
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    public CreateOrder(OrderRepository orderRepo, ProductRepository productRepo, Publisher publisher) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.publisher = publisher;
    }

    public CreateOrderDTO.Output execute(CreateOrderDTO.Input input) {
        var products = productRepo
                .valuesByIds(input.items().stream().map(CreateOrderDTO.Input.OrderItem::id).toList())
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

        publisher.publish(new OrderCreated(order.id()));

        return new CreateOrderDTO.Output(order.id(), order.status());
    }
}
