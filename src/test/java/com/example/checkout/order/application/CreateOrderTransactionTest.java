package com.example.checkout.order.application;

import com.example.checkout.order.application.dto.CreateOrderDTO;
import com.example.checkout.order.domain.OrderRepository;
import com.example.checkout.order.domain.event.OrderCreated;
import com.example.checkout.shared.domain.Event;
import com.example.checkout.shared.infrastructure.queue.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class CreateOrderTransactionTest {

    static class FailingPublisher implements Publisher {
        final List<Event> attempted = new ArrayList<>();

        @Override
        public <T extends Event> void publish(T event) {
            attempted.add(event);
            throw new IllegalStateException("outbox unavailable");
        }
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        FailingPublisher failingPublisher() {
            return new FailingPublisher();
        }
    }

    @Autowired
    private CreateOrder createOrder;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FailingPublisher publisher;

    @Test
    void ShouldNotSaveOrderWhenEventCannotBePublished() {
        var input = new CreateOrderDTO.Input(
                UUID.randomUUID(),
                List.of(new CreateOrderDTO.Input.OrderItem(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), 1))
        );

        Assertions.assertThrows(IllegalStateException.class, () -> createOrder.execute(input));

        var orderId = ((OrderCreated) publisher.attempted.getFirst()).orderId();
        Assertions.assertTrue(orderRepository.getById(orderId).isEmpty());
    }
}
