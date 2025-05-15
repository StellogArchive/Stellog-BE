package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class StarbucksReviewNotFoundException extends NotFoundGroupException {
    public StarbucksReviewNotFoundException(String message) {
        super(message);
    }

    public StarbucksReviewNotFoundException() {
        this("해당 스타벅스 리뷰를 찾을 수 없습니다.");
    }
}
