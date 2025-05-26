package org.example.stellog.starbucks.api.dto.response;

public record StarbucksInfoResDto(
        Long id,
        String name,
        double latitude,
        double longitude
) {
}
