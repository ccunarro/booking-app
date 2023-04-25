package com.ccunarro.hostfully.data.common;

import java.time.LocalDate;

public class DateRange {

    private LocalDate start;
    private LocalDate end;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.start = startDate;
        this.end = endDate;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
