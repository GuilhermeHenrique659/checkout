package com.example.checkout.shared.infrastructure.queue;

import com.example.checkout.shared.domain.Event;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.queue.provider",
        havingValue = "kafka"
)
public class KafkaPublisher implements Publisher {
    private final KafkaTemplate<String, Object> kafka;
    private final ConcurrentKafkaListenerContainerFactory<String, Object> factory;

    public KafkaPublisher(
            KafkaTemplate<String, Object> kafka,
            ConcurrentKafkaListenerContainerFactory<String, Object> factory
    ) {
        this.factory = factory;
        this.kafka = kafka;
    }

    @Override
    public <T extends Event> void publish(T event) {
        kafka.send(event.getClass().getSimpleName(), event);
    }
}
