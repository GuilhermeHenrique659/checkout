package com.example.checkout.inventory.domain;

import com.example.checkout.shared.domain.DomainError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class ReservationTest {

    @Test
    void shouldNotCreateReservationWithAnAlreadyReservedUnit() {
        var productId = UUID.randomUUID();
        var reserved = Unit.create(productId, "P1-0001");
        reserved.reserve();

        Assertions.assertThrows(DomainError.class, () -> Reservation.create(
                UUID.randomUUID(),
                List.of(Unit.create(productId, "P1-0002"), reserved)
        ));
    }
}
