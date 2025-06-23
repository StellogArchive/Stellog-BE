package org.example.stellog.calendar.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class CalendarNotFoundException extends NotFoundGroupException {
    public CalendarNotFoundException(String message) {
        super(message);
    }
}
