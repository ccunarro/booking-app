package com.ccunarro.hostfully.data.reservation;

import com.ccunarro.hostfully.data.reservation.dtos.ReservationDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDto map(Reservation entity) {
        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setPropertyId(entity.getPropertyId());
        dto.setStartDate(entity.getStart());
        dto.setEndDate(entity.getEnd());
        dto.setType(entity.getType());
        return dto;
    }
}
