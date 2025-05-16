package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.AccessDeniedGroupException;

public class UnauthorizedReviewAccessException extends AccessDeniedGroupException {
    public UnauthorizedReviewAccessException(String message) {
        super(message);
    }
}
