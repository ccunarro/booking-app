package com.ccunarro.hostfully.security;

import com.ccunarro.hostfully.data.property.PropertyRepository;
import com.ccunarro.hostfully.data.reservation.ReservationRepository;
import com.ccunarro.hostfully.data.user.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("userSecurity")
public class UserSecurity {

    private final ReservationRepository reservationRepository;
    private final PropertyRepository propertyRepository;

    public UserSecurity(ReservationRepository reservationRepository,
                        PropertyRepository propertyRepository) {
        this.reservationRepository = reservationRepository;
        this.propertyRepository = propertyRepository;
    }

    public boolean userOwnsBooking(SimpleUserDetails simpleUserDetails, UUID bookingId) {
        User user = simpleUserDetails.getUser();
        return reservationRepository.existsBookingForUser(bookingId, user.getId());
    }

    public boolean userOwnsBlock(SimpleUserDetails simpleUserDetails, UUID blockId) {
        User user = simpleUserDetails.getUser();
        return reservationRepository.existsBlockForUser(blockId, user.getId());
    }

    public boolean userOwnsProperty(SimpleUserDetails simpleUserDetails, UUID propertyId) {
        User user = simpleUserDetails.getUser();
        return propertyRepository.existsPropertyForUser(propertyId, user.getId());
    }
}
