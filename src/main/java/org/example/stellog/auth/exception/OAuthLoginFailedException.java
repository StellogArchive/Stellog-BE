package org.example.stellog.auth.exception;

import org.example.stellog.global.error.exception.AuthGroupException;

public class OAuthLoginFailedException extends AuthGroupException {
    public OAuthLoginFailedException(String message) {
        super("OAuth 로그인 실패: " + message);
    }
}
