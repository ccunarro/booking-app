package com.ccunarro.hostfully.data.reservation.forms;

import com.ccunarro.hostfully.data.common.DateRange;

public class UpdateBookingForm {

    private DateRange date;

    public UpdateBookingForm() {
    }

    public UpdateBookingForm(DateRange date) {
       this.date = date;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
    }
}
