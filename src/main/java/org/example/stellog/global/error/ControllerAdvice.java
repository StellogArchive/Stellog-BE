package org.example.stellog.global.error;

import org.example.stellog.global.error.dto.ErrorResponse;
import org.example.stellog.global.error.exception.AccessDeniedGroupException;
import org.example.stellog.global.error.exception.AuthGroupException;
import org.example.stellog.global.error.exception.InvalidGroupException;
import org.example.stellog.global.error.exception.NotFoundGroupException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({AccessDeniedGroupException.class})
    public ResponseEntity<ErrorResponse> handlerAccessDeniedData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthGroupException.class})
    public ResponseEntity<ErrorResponse> handlerAuthData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidGroupException.class})
    public ResponseEntity<ErrorResponse> handlerInvalidData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundGroupException.class})
    public ResponseEntity<ErrorResponse> handlerNotFoundData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
