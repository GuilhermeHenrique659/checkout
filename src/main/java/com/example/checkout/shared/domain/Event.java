package com.example.checkout.shared.domain;

import java.time.Instant;
import java.util.UUID;

public interface Event {
    UUID eventId();

    Instant occurredAt();
}
