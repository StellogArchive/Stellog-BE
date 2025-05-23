package org.example.stellog.follow.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class NotFollowingException extends NotFoundGroupException {
    public NotFollowingException(String message) {
        super(message);
    }
}

