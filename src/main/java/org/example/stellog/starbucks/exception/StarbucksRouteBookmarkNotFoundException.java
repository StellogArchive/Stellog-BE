package org.example.stellog.starbucks.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class StarbucksRouteBookmarkNotFoundException extends NotFoundGroupException {
    public StarbucksRouteBookmarkNotFoundException(String message) {
        super(message);
    }

}
