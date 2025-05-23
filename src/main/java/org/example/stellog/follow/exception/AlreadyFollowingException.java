package org.example.stellog.follow.exception;

import org.example.stellog.global.error.exception.InvalidGroupException;

public class AlreadyFollowingException extends InvalidGroupException {
    public AlreadyFollowingException(String message) {
        super(message);
    }
}
