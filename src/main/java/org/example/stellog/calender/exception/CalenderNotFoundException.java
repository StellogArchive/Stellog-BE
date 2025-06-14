package org.example.stellog.calender.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class CalenderNotFoundException extends NotFoundGroupException {
    public CalenderNotFoundException(String message) {
        super(message);
    }
}
