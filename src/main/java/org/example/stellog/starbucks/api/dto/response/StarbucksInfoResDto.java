package org.example.stellog.starbucks.api.dto.response;

public record StarbucksInfoResDto(
        Long id,
        String starbucksName,
        double latitude,
        double longitude
) {
}
