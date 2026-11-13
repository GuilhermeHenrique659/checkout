package com.example.checkout.inventory.application;

import com.example.checkout.inventory.domain.UnitStatus;
import com.example.checkout.inventory.application.dto.ReserveProductDTO;
import com.example.checkout.inventory.domain.Unit;
import com.example.checkout.inventory.domain.ReservationStatus;
import com.example.checkout.inventory.infrastructure.FakeReservationRepository;
import com.example.checkout.inventory.infrastructure.FakeUnitRepository;
import com.example.checkout.shared.domain.ApplicationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class ReserveProductTest {

    @Test
    void shouldReserveAProduct() {
        var product1Id = UUID.randomUUID();
        var product2Id = UUID.randomUUID();
        var referenceId = UUID.randomUUID();

        var unitRepository = new FakeUnitRepository(
                List.of(
                        Unit.create(product1Id, "P1-0001"),
                        Unit.create(product1Id, "P1-0002"),
                        Unit.create(product1Id, "P1-0003"),
                        Unit.create(product2Id, "P2-0001"),
                        Unit.create(product2Id, "P2-0002")
                )
        );
        var reservationRepository = new FakeReservationRepository();
        var reserveProduct = new ReserveProduct(
                unitRepository,
                reservationRepository
        );

        var output = reserveProduct.execute(new ReserveProductDTO.Input(
                referenceId,
                List.of(
                        new ReserveProductDTO.ProductItem(product1Id, 2),
                        new ReserveProductDTO.ProductItem(product2Id, 2)
                )
        ));

        var reservedProducts = unitRepository.getValuesByProductIdAndStatus(Map.of(product1Id, 10, product2Id, 10), UnitStatus.RESERVED);
        Assertions.assertEquals(4, reservedProducts.size());

        var reservation = reservationRepository.findByReferenceId(referenceId).orElseThrow();
        Assertions.assertEquals(output.reservationId(), reservation.id());
        Assertions.assertEquals(ReservationStatus.ACTIVE, reservation.status());
        Assertions.assertEquals(
                reservedProducts.stream().map(Unit::id).collect(Collectors.toSet()),
                Set.copyOf(reservation.unitIds())
        );
    }

    @Test
    void shouldTryReserveProductOutOfStock() {
        var product1Id = UUID.randomUUID();
        var product2Id = UUID.randomUUID();
        var referenceId = UUID.randomUUID();

        var unitRepository = new FakeUnitRepository(
                List.of(
                        Unit.create(product1Id, "P1-0001"),
                        Unit.create(product2Id, "P2-0001"),
                        Unit.create(product2Id, "P2-0002")
                )
        );
        var reservationRepository = new FakeReservationRepository();
        var reserveProduct = new ReserveProduct(
                unitRepository,
                reservationRepository
        );

        Assertions.assertThrows(ApplicationError.class, () -> reserveProduct.execute(new ReserveProductDTO.Input(
                referenceId,
                List.of(
                        new ReserveProductDTO.ProductItem(product1Id, 2),
                        new ReserveProductDTO.ProductItem(product2Id, 2)
                )
        )));
        Assertions.assertTrue(reservationRepository.findByReferenceId(referenceId).isEmpty());
    }
}
