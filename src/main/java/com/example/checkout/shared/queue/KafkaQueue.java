package com.example.checkout.shared.queue;

import com.example.checkout.shared.domain.Event;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaQueue implements Queue {
    private final KafkaTemplate<String, Object> kafka;
    private final ConcurrentKafkaListenerContainerFactory<String, Object> factory;

    public KafkaQueue(
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


    //TODO: ADD CONSUMER REGISTER
    @Override
    public <T extends Event> void subscriber(Class<T> event, Subscriber<T> sub) {

    }
}
