package com.ccunarro.hostfully.services;

import com.ccunarro.hostfully.data.common.DateRange;
import com.ccunarro.hostfully.data.reservation.Reservation;
import com.ccunarro.hostfully.data.reservation.ReservationMapper;
import com.ccunarro.hostfully.data.reservation.ReservationRepository;
import com.ccunarro.hostfully.data.reservation.dtos.ReservationDto;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private static final Map<UUID, Boolean> LOCKS = new ConcurrentHashMap<>();

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public ReservationDto findById(UUID bookingId) {
        Optional<Reservation> optionalBooking = reservationRepository.findById(bookingId);

        if (!optionalBooking.isPresent()) {
            throw new ResourceNotFoundException("Booking not found");
        }
        return reservationMapper.map(optionalBooking.get());
    }

    public List<ReservationDto> findAllBookingsByUserId(UUID userId) {
        List<Reservation> bookings = reservationRepository.findAllByUserIdAAndType(userId, Reservation.Type.BOOKING);
        return bookings.stream().map(r -> reservationMapper.map(r)).collect(Collectors.toList());
    }

    public void deleteReservation(UUID reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (!optionalReservation.isPresent()) {
            throw new ResourceNotFoundException("Reservation not found");
        }
        reservationRepository.delete(optionalReservation.get());
    }

    public void updateBookingDates(UUID bookingId, DateRange date) {
        Optional<Reservation> optionalBooking = reservationRepository.findById(bookingId);

        if (!optionalBooking.isPresent()) {
            throw new ResourceNotFoundException("Booking not found");
        }

        Reservation booking = optionalBooking.get();
        UUID propertyId = booking.getPropertyId();
        LocalDate start = date.getStart();
        LocalDate end = date.getEnd();

        if (!reservationRepository.isAvailableOnDates(propertyId, start, end)) {
            throw new DateRangeNotAvailableException("Selected dates not available for booking");
        }

        booking.setStart(start);
        booking.setEnd(end);
    }

    /**
     *
     * This code is just for the exercise to avoid concurrent reservations on the same property
     * (it's a very basic approach just to avoid the issue of ending with overlapped reservations)
     * {@see com.ccunarro.hostfully.services.ReservationServiceIntTest#concurrentOverlappingReservations}).
     *
     * I was not able to find an easy way to write a constraint on H2 for this problem, but that would be
     * a much better approach than the one below.
     *
     * On Postgresql seems it is not a complicated task
     * https://dba.stackexchange.com/questions/110582/uniqueness-constraint-with-date-range
     *
     * @param userId
     * @param form
     * @param type
     * @return
     */
    public ReservationDto createReservation(UUID userId, CreateReservationForm form, Reservation.Type type) {
        synchronized (form.getPropertyId()) {
            if (LOCKS.containsKey(form.getPropertyId())) {
                throw new ConcurrentBookingException();
            }
            LOCKS.put(form.getPropertyId(), true);
        }
        try {
            return executeCreateReservation(userId, form, type);
        } finally {
            LOCKS.remove(form.getPropertyId());
        }
    }

    public ReservationDto executeCreateReservation(UUID userId, CreateReservationForm form, Reservation.Type type) {
        UUID propertyId = form.getPropertyId();
        LocalDate start = form.getDate().getStart();
        LocalDate end = form.getDate().getEnd();

        if (!reservationRepository.isAvailableOnDates(propertyId, start, end)) {
            throw new DateRangeNotAvailableException("Selected dates not available for booking");
        }
        Reservation booking = reservationRepository.save(new Reservation(propertyId, userId, start, end, type));
        return reservationMapper.map(booking);
    }

}
