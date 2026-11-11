package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.ProcessPaymentInput;
import com.example.checkout.order.domain.OrderRepository;
import com.example.checkout.order.infrastructure.gateway.PaymentProcessor;
import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.infrastructure.queue.Publisher;
import org.springframework.stereotype.Service;

@Service
public class ProcessPayment {
    private final OrderRepository orderRepo;
    private final PaymentProcessor payment;
    private final Publisher publisher;

    public ProcessPayment(OrderRepository orderRepo, PaymentProcessor payment, Publisher publisher) {
        this.orderRepo = orderRepo;
        this.payment = payment;
        this.publisher = publisher;
    }

    public void execute(ProcessPaymentInput input) {
        var order = orderRepo.getById(input.orderId()).orElseThrow(() -> new ApplicationError("ORDER_NOT_FOUND", "Order Not found"));

        var result = payment.process(order.customerId(), order.id(), order.total());

        var newStatus = result.status().equals("SUCCESS") ? "PAID" : "CANCEL";

        order.changeStatus(newStatus);

        orderRepo.save(order);
        order.events().forEach(publisher::publish);
    }
}
