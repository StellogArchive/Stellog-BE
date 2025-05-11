package org.example.stellog.room.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class RoomNotFoundException extends NotFoundGroupException {
    public RoomNotFoundException(String message) {
        super(message);
    }

    public RoomNotFoundException() {
        this("해당 방을 찾을 수 없습니다.");
    }
}
