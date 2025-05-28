package org.example.stellog.starbucks.exception;

import org.example.stellog.global.error.exception.AccessDeniedGroupException;

public class DuplicateStarbucksRouteLikeException extends AccessDeniedGroupException {
    public DuplicateStarbucksRouteLikeException(String message) {
        super(message);
    }

    public DuplicateStarbucksRouteLikeException() {
        this("이미 좋아요를 누른 최적화동선입니다.");
    }
}
