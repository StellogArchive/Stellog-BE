package org.example.stellog.starbucks.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class StarbucksRouteNotFoundException extends NotFoundGroupException {
    public StarbucksRouteNotFoundException(String message) {
        super(message);
    }
}
