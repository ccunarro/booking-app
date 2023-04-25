package com.ccunarro.hostfully.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ConcurrentBookingException extends RuntimeException {

    public ConcurrentBookingException() {
        super();
    }

    public ConcurrentBookingException(String message) {
        super(message);
    }
}
