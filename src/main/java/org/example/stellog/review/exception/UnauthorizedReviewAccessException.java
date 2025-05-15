package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.AccessDeniedGroupException;

public class UnauthorizedReviewAccessException extends AccessDeniedGroupException {
    public UnauthorizedReviewAccessException(String message) {
        super(message);
    }

    public UnauthorizedReviewAccessException() {
        this("해당 리뷰에 접근할 수 있는 권한이 없습니다.");
    }
}
