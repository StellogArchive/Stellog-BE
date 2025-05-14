package org.example.stellog.room.exception;

import org.example.stellog.global.error.exception.AccessDeniedGroupException;

public class UnauthorizedRoomAccessException extends AccessDeniedGroupException {
    public UnauthorizedRoomAccessException(String message) {
        super(message);
    }
}
