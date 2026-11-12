package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.publisher.provider",
        havingValue = "outbox"
)
public class OutboxPublisher implements Publisher {
    @Override
    public <T extends Event> void publish(T event) {
        
    }
}
