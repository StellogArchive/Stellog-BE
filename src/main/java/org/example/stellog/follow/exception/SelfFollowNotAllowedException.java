package org.example.stellog.follow.exception;

import org.example.stellog.global.error.exception.AccessDeniedGroupException;

public class SelfFollowNotAllowedException extends AccessDeniedGroupException {
    public SelfFollowNotAllowedException(String message) {
        super(message);
    }
}
