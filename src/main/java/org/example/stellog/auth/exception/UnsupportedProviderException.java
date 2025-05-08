package org.example.stellog.auth.exception;

import org.example.stellog.global.error.exception.AuthGroupException;

public class UnsupportedProviderException extends AuthGroupException {
    public UnsupportedProviderException(String provider) {
        super("지원하지 않는 OAuth Provider입니다.: " + provider);
    }
}
