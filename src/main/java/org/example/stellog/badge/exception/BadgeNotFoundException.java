package org.example.stellog.badge.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class BadgeNotFoundException extends NotFoundGroupException {
    public BadgeNotFoundException(String message) {
        super(message);
    }

    public BadgeNotFoundException() {
        super("존재하지 않는 배지입니다.");
    }
}
