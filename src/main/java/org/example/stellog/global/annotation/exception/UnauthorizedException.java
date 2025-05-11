package org.example.stellog.global.annotation.exception;

import org.example.stellog.global.error.exception.AuthGroupException;

public class UnauthorizedException extends AuthGroupException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
