package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class ReviewNotFoundException extends NotFoundGroupException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
