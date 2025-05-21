package org.example.stellog.global.gcs.exception;

import org.example.stellog.global.error.exception.NotFoundGroupException;

public class GCSFileNotFoundException extends NotFoundGroupException {
    public GCSFileNotFoundException(String message) {
        super(message);
    }
}
