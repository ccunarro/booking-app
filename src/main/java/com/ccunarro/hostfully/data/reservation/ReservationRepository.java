package com.ccunarro.hostfully.data.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query( " SELECT count(r) = 0 FROM Reservation r " +
            " WHERE r.propertyId = :propertyId " +
            " AND (:start >= r.start AND :start < r.end " +
            "      OR (:end > r.start AND :end <= r.end))")
    boolean isAvailableOnDates(UUID propertyId, LocalDate start, LocalDate end);

    @Query(" SELECT count(r) = 1 FROM Reservation r " +
           " WHERE r.id = :reservationId AND r.userId = :userId AND r.type = 'BOOKING'")
    boolean existsBookingForUser(UUID reservationId, UUID userId);

    @Query(" SELECT count(r) = 1 FROM Reservation r " +
           " WHERE r.id = :reservationId AND r.userId = :userId AND r.type = 'BLOCK'")
    boolean existsBlockForUser(UUID reservationId, UUID userId);

    @Query(" SELECT r FROM Reservation r " +
           " WHERE r.userId = :userId and r.type = :type")
    List<Reservation> findAllByUserIdAAndType(UUID userId, Reservation.Type type);
}
