package com.example.checkout.inventory.application;

import com.example.checkout.inventory.application.dto.ReserveProductDTO;
import com.example.checkout.inventory.domain.Unit;
import com.example.checkout.inventory.domain.UnitRepository;
import com.example.checkout.shared.domain.ApplicationError;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReserveProduct {
    private final UnitRepository unitRepository;

    public ReserveProduct(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public ReserveProductDTO.Output execute(ReserveProductDTO.Input input) {
        var units = unitRepository.getValuesByProductIdAndStatus(
                input.productItems()
                        .stream()
                        .collect(Collectors.toMap(
                                ReserveProductDTO.ProductItem::productId,
                                ReserveProductDTO.ProductItem::quantity,
                                Integer::sum
                        )),
                "AVAILABLE");

        for (var product : input.productItems()) {
            var unitsToReserve = units.stream()
                    .filter(unit -> product.productId().equals(unit.productId()))
                    .toList();

            if (unitsToReserve.size() < product.quantity()) {
                throw new ApplicationError("PRODUCT_UNAVAILABLE", "Product quantity is not available");
            }

            unitsToReserve.forEach(Unit::reserve);
        }

        unitRepository.saveMany(units);

        return new ReserveProductDTO.Output(List.of());
    }
}
