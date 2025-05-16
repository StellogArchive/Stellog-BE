package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class ReviewMemberNotFoundException extends NotFoundGroupException {
    public ReviewMemberNotFoundException(String message) {
        super(message);
    }
}
