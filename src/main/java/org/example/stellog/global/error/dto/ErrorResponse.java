package org.example.stellog.global.error.dto;

public record ErrorResponse(
    int statusCode,
    String message
) {

}
