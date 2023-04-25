package com.ccunarro.hostfully.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DateRangeNotAvailableException extends RuntimeException {

    public DateRangeNotAvailableException(String message) {
        super(message);
    }
}
