package org.example.stellog.gcs.exception;

import java.io.IOException;
import org.example.stellog.global.error.exception.NotFoundGroupException;

public class GCSFileNotFoundException extends NotFoundGroupException {
    public GCSFileNotFoundException(String message, IOException e) {
        super(message);
    }
}
