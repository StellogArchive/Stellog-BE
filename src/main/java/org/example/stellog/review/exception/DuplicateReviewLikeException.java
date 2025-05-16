package org.example.stellog.review.exception;

import org.example.stellog.global.error.exception.AccessDeniedGroupException;

public class DuplicateReviewLikeException extends AccessDeniedGroupException {
    public DuplicateReviewLikeException(String message) {
        super(message);
    }

    public DuplicateReviewLikeException() {
        this("이미 좋아요를 누른 리뷰입니다.");
    }
}
