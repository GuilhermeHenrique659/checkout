package com.example.checkout.inventory.application;

import com.example.checkout.inventory.domain.UnitStatus;
import com.example.checkout.inventory.application.dto.ReserveProductDTO;
import com.example.checkout.inventory.domain.Reservation;
import com.example.checkout.inventory.domain.ReservationRepository;
import com.example.checkout.inventory.domain.Unit;
import com.example.checkout.inventory.domain.UnitRepository;
import com.example.checkout.shared.domain.ApplicationError;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ReserveProduct {
    private final UnitRepository unitRepository;
    private final ReservationRepository reservationRepository;

    public ReserveProduct(UnitRepository unitRepository, ReservationRepository reservationRepository) {
        this.unitRepository = unitRepository;
        this.reservationRepository = reservationRepository;
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
                UnitStatus.AVAILABLE);

        var unitsToReserve = new ArrayList<Unit>();

        for (var product : input.productItems()) {
            var productUnits = units.stream()
                    .filter(unit -> product.productId().equals(unit.productId()))
                    .toList();

            if (productUnits.size() < product.quantity()) {
                throw new ApplicationError("PRODUCT_UNAVAILABLE", "Product quantity is not available");
            }

            unitsToReserve.addAll(productUnits);
        }

        var reservation = Reservation.create(input.referenceId(), unitsToReserve);

        unitRepository.saveMany(units);
        reservationRepository.save(reservation);

        return new ReserveProductDTO.Output(reservation.id(), input.productItems());
    }
}
