package com.ccunarro.hostfully.data.reservation.forms;

import com.ccunarro.hostfully.data.common.DateRange;
import com.ccunarro.hostfully.validation.annotation.ValidDateRange;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateReservationForm {

    @NotNull
    private UUID propertyId;

    @ValidDateRange
    private DateRange date;

    public CreateReservationForm() {
    }

    public CreateReservationForm(UUID propertyId, DateRange date) {
        this.propertyId = propertyId;
        this.date = date;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
    }
}
