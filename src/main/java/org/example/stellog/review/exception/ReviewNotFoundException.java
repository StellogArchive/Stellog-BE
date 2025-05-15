package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class ReviewNotFoundException extends NotFoundGroupException {
    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException() {
        this("해당 리뷰를 찾을 수 없습니다.");
    }
}
