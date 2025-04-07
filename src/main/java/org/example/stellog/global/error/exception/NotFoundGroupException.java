package org.example.stellog.global.error.exception;

public abstract class NotFoundGroupException extends RuntimeException{
    public NotFoundGroupException(String message) {
        super(message);
    }
}
