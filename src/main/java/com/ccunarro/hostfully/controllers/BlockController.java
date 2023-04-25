package com.ccunarro.hostfully.controllers;

import com.ccunarro.hostfully.data.reservation.Reservation;
import com.ccunarro.hostfully.data.reservation.dtos.ReservationDto;
import com.ccunarro.hostfully.data.reservation.forms.CreateReservationForm;
import com.ccunarro.hostfully.data.user.User;
import com.ccunarro.hostfully.security.SimpleUserDetails;
import com.ccunarro.hostfully.services.ReservationService;
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
public class BlockController {

    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(BlockController.class.getName());

    public BlockController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("isAuthenticated() and @userSecurity.userOwnsProperty(#userDetails, #blockForm.propertyId)")
    @RequestMapping(value = "/block/", method = RequestMethod.POST)
    public ResponseEntity createBlock(@RequestBody @Valid CreateReservationForm blockForm,
                                      @AuthenticationPrincipal SimpleUserDetails userDetails) {
        User user = userDetails.getUser();
        ReservationDto reservationDto = reservationService.createReservation(user.getId(), blockForm, Reservation.Type.BLOCK);
        logger.debug("Success creating block {}", reservationDto);
        return new ResponseEntity<>(reservationDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and @userSecurity.userOwnsBlock(#userDetails, #blockId)")
    @RequestMapping(value = "/block/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity deleteBlock(@PathVariable("id") UUID blockId,
                                      @AuthenticationPrincipal SimpleUserDetails userDetails) {
        reservationService.deleteReservation(blockId);
        logger.debug("Success deleting block {}", blockId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
