package com.ccunarro.hostfully.controllers;

import com.ccunarro.hostfully.data.reservation.Reservation;
import com.ccunarro.hostfully.data.reservation.dtos.ReservationDto;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import com.ccunarro.hostfully.data.reservation.forms.UpdateBookingForm;
import com.ccunarro.hostfully.data.user.User;
import com.ccunarro.hostfully.security.SimpleUserDetails;
import com.ccunarro.hostfully.services.ReservationService;
import com.ccunarro.hostfully.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class BookingController
{

    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class.getName());

    public BookingController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("isAuthenticated() and @userSecurity.userOwnsBooking(#userDetails, #bookingId)")
    @RequestMapping(value = "/booking/{id}/", method = RequestMethod.GET)
    public ResponseEntity getBooking(@PathVariable("id") UUID bookingId,
                                     @AuthenticationPrincipal SimpleUserDetails userDetails) {
        ReservationDto reservationDto = reservationService.findById(bookingId);
        logger.debug("Success loading booking {}", reservationDto);
        return new ResponseEntity<>(reservationDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/booking/", method = RequestMethod.POST)
    public ResponseEntity createBooking(@RequestBody @Valid CreateReservationForm bookingForm,
                                        @AuthenticationPrincipal SimpleUserDetails userDetails) {
        User user = userDetails.getUser();
        ReservationDto reservationDto = reservationService.createReservation(user.getId(), bookingForm, Reservation.Type.BOOKING);
        logger.debug("Success creating booking {}", reservationDto);
        return new ResponseEntity<>(reservationDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and @userSecurity.userOwnsBooking(#userDetails, #bookingId)")
    @RequestMapping(value = "/booking/{id}/", method = RequestMethod.PUT)
    public ResponseEntity updateBookingDates(@PathVariable("id") UUID bookingId,
                                             @AuthenticationPrincipal SimpleUserDetails userDetails,
                                             @RequestBody @Valid UpdateBookingForm bookingForm) {
        reservationService.updateBookingDates(bookingId, bookingForm.getDate());
        logger.debug("Success updating booking dates for {}", bookingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and @userSecurity.userOwnsBooking(#userDetails, #bookingId)")
    @RequestMapping(value = "/booking/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity deleteBooking(@PathVariable("id") UUID bookingId,
                                        @AuthenticationPrincipal SimpleUserDetails userDetails) {
        reservationService.deleteReservation(bookingId);
        logger.debug("Success cancelling booking {}", bookingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
