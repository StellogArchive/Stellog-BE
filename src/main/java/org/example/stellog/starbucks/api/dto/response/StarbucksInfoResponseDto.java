package org.example.stellog.starbucks.api.dto.response;

public record StarbucksInfoResponseDto(
        Long id,
        String starbucksName,
        double latitude,
        double longitude
) {
}
