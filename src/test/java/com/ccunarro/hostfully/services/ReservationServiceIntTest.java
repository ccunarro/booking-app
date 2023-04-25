package com.ccunarro.hostfully.services;

import com.ccunarro.hostfully.BookingTestUtils;
import com.ccunarro.hostfully.data.reservation.Reservation;
import com.ccunarro.hostfully.data.reservation.dtos.ReservationDto;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
@DirtiesContext
class ReservationServiceIntTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    public void createReservationOk(){
        System.out.println("Given the correct booking information it should create successfully the reservation");
        UUID userId = UUID.fromString(BookingTestUtils.USER_ID_FOO);
        LocalDate start = LocalDate.now().plusDays(40);
        LocalDate end = LocalDate.now().plusDays(43);
        final CreateReservationForm form = BookingTestUtils.buildReservationFormWithDates(start, end);
        ReservationDto result = reservationService.createReservation(userId, form, Reservation.Type.BOOKING);
        Assertions.assertTrue(reservationService.findById(result.getId()) != null);
    }

    @Test
    public void createReservationsWithOverlapping(){
        System.out.println("Given an attempt to create a second reservation that overlaps " +
                "with a previous one, it should fail and throw DateRangeNotAvailableException");
        UUID userId = UUID.fromString(BookingTestUtils.USER_ID_FOO);
        LocalDate start = LocalDate.now().plusDays(70);
        LocalDate end = LocalDate.now().plusDays(73);
        final CreateReservationForm form = BookingTestUtils.buildReservationFormWithDates(start, end);
        reservationService.createReservation(userId, form, Reservation.Type.BOOKING);

        start = LocalDate.now().plusDays(72);
        end = LocalDate.now().plusDays(76);
        final CreateReservationForm otherForm = BookingTestUtils.buildReservationFormWithDates(start, end);

        assertThrowsExactly(DateRangeNotAvailableException.class, () -> {
            reservationService.createReservation(userId, otherForm, Reservation.Type.BOOKING);
        });
    }

    @Test
    public void concurrentOverlappingReservations() throws InterruptedException {
        System.out.println("Given an attempt to create three reservations concurrently that overlap each other, " +
                " it should end up only with one");
        UUID userId = UUID.fromString(BookingTestUtils.USER_ID_NEW_GUEST);
        Assertions.assertTrue(reservationService.findAllBookingsByUserId(userId).size() == 0,
                "User has no bookings");


        LocalDate firstStart = LocalDate.now().plusDays(20);
        LocalDate firstEnd = LocalDate.now().plusDays(40);
        final var firstOverlap = BookingTestUtils.buildReservationForm(firstStart,
                firstEnd, BookingTestUtils.PROPERTY_ID_HOUSE_3);

        LocalDate secondStart = LocalDate.now().plusDays(25);
        LocalDate secondEnd = LocalDate.now().plusDays(35);
        final var secondOverlap = BookingTestUtils.buildReservationForm(secondStart,
                secondEnd, BookingTestUtils.PROPERTY_ID_HOUSE_3);

        LocalDate thirdStart = LocalDate.now().plusDays(28);
        LocalDate thirdEnd = LocalDate.now().plusDays(30);
        final var thirdOverlap = BookingTestUtils.buildReservationForm(thirdStart,
                thirdEnd, BookingTestUtils.PROPERTY_ID_HOUSE_3);

        Map<Integer, CreateReservationForm> map = new HashMap<>();
        map.put(1, firstOverlap);
        map.put(2, secondOverlap);
        map.put(3, thirdOverlap);

        Map<Integer, CompletableFuture> futures = new HashMap<>();

        for (int i = 1; i <= 3; i++) {
            final var form = map.get(i);
            futures.put(i, CompletableFuture.supplyAsync(() ->
                    reservationService.createReservation(userId, form, Reservation.Type.BOOKING)));
        }
        CompletableFuture<Void> combinedFutures = CompletableFuture.allOf(futures.get(1), futures.get(2), futures.get(3));

        try {
            combinedFutures.get();
        } catch (ExecutionException e) {}

        Assertions.assertEquals(1, reservationService.findAllBookingsByUserId(userId).size(),
                "User has 1 booking");

    }

}