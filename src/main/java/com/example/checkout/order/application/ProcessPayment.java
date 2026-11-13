package com.example.checkout.order.application;

import com.example.checkout.order.domain.OrderStatus;
import com.example.checkout.order.application.dto.ProcessPaymentInput;
import com.example.checkout.order.domain.OrderRepository;
import com.example.checkout.order.infrastructure.gateway.PaymentProcessor;
import com.example.checkout.shared.domain.ApplicationError;
import com.example.checkout.shared.infrastructure.queue.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

        // Duplicate delivery of the same payment: the order was already handled, so leave it untouched.
        if (result.status().equals("ALREADY_PROCESSED")) return;

        var newStatus = result.status().equals("SUCCESS") ? OrderStatus.PAID : OrderStatus.CANCEL;

        order.changeStatus(newStatus);

        orderRepo.save(order);
        order.events().forEach(publisher::publish);
    }
}
