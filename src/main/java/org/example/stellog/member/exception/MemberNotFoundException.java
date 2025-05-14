package org.example.stellog.member.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class MemberNotFoundException extends NotFoundGroupException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        this("해당 사용자를 찾을 수 없습니다.");
    }
}
