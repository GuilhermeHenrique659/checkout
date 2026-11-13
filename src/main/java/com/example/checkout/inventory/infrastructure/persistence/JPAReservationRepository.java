package com.example.checkout.inventory.infrastructure.persistence;

import com.example.checkout.inventory.domain.Reservation;
import com.example.checkout.inventory.domain.ReservationRepository;
import com.example.checkout.inventory.domain.ReservationStatus;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JPAReservationRepository implements ReservationRepository {
    private final ReservationDao reservationDao;

    public JPAReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public Reservation save(Reservation reservation) {
        reservationDao.save(new ReservationModel(
                reservation.id(),
                reservation.referenceId(),
                reservation.status().name(),
                reservation.createdAt(),
                new HashSet<>(reservation.unitIds())
        ));

        return reservation;
    }

    @Override
    public Optional<Reservation> findByReferenceId(UUID referenceId) {
        return reservationDao.findFirstByReferenceIdOrderByCreatedAtDesc(referenceId)
                .map(model -> new Reservation(
                        model.getId(),
                        model.getReferenceId(),
                        ReservationStatus.valueOf(model.getStatus()),
                        model.getCreatedAt(),
                        List.copyOf(model.getUnitIds())
                ));
    }
}
