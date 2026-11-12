package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.publisher.provider",
        havingValue = "spring",
        matchIfMissing = true
)
public class SpringPublisher implements Publisher {
    private final ApplicationEventPublisher publisher;

    public SpringPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public <T extends Event> void publish(T event) {
        publisher.publishEvent(event);
    }
}
