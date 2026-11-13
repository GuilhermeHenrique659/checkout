package com.example.checkout.inventory.application;

import com.example.checkout.inventory.domain.UnitStatus;
import com.example.checkout.inventory.application.dto.ReserveProductDTO;
import com.example.checkout.inventory.domain.Unit;
import com.example.checkout.inventory.infrastructure.FakeUnitRepository;
import com.example.checkout.shared.domain.ApplicationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

class ReserveProductTest {

    @Test
    void shouldReserveAProduct() {
        var product1Id = UUID.randomUUID();
        var product2Id = UUID.randomUUID();

        var unitRepository = new FakeUnitRepository(
                List.of(
                        Unit.create(product1Id, "P1-0001"),
                        Unit.create(product1Id, "P1-0002"),
                        Unit.create(product1Id, "P1-0003"),
                        Unit.create(product2Id, "P2-0001"),
                        Unit.create(product2Id, "P2-0002")
                )
        );
        var reserveProduct = new ReserveProduct(
                unitRepository
        );

        var output = reserveProduct.execute(new ReserveProductDTO.Input(
                List.of(
                        new ReserveProductDTO.ProductItem(product1Id, 2),
                        new ReserveProductDTO.ProductItem(product2Id, 2)
                )
        ));

        var reservedProducts = unitRepository.getValuesByProductIdAndStatus(Map.of(product1Id, 2, product2Id, 2), UnitStatus.RESERVED);
        Assertions.assertEquals(4, reservedProducts.size());
    }

    @Test
    void shouldTryReserveProductOutOfStock() {
        var product1Id = UUID.randomUUID();
        var product2Id = UUID.randomUUID();

        var unitRepository = new FakeUnitRepository(
                List.of(
                        Unit.create(product1Id, "P1-0001"),
                        Unit.create(product2Id, "P2-0001"),
                        Unit.create(product2Id, "P2-0002")
                )
        );
        var reserveProduct = new ReserveProduct(
                unitRepository
        );

        Assertions.assertThrows(ApplicationError.class, () -> reserveProduct.execute(new ReserveProductDTO.Input(
                List.of(
                        new ReserveProductDTO.ProductItem(product1Id, 2),
                        new ReserveProductDTO.ProductItem(product2Id, 2)
                )
        )));
    }
}
