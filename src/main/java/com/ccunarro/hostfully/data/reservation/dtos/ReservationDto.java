package com.ccunarro.hostfully.data.reservation.dtos;

import com.ccunarro.hostfully.data.reservation.Reservation;

import java.time.LocalDate;
import java.util.UUID;

public class ReservationDto {

    private UUID id;
    private UUID propertyId;
    private UUID userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Reservation.Type type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Reservation.Type getType() {
        return type;
    }

    public void setType(Reservation.Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ReservationDto{" +
                "id=" + id +
                ", propertyId=" + propertyId +
                ", userId=" + userId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type=" + type +
                '}';
    }
}
