package org.example.stellog.starbucks;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class StarbucksNotFoundException extends NotFoundGroupException {
    public StarbucksNotFoundException(String message) {
        super(message);
    }

    public StarbucksNotFoundException() {
        this("해당 스타벅스를 찾을 수 없습니다.");
    }
}
