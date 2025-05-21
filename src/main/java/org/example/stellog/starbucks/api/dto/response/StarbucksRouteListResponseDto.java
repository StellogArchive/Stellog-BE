package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteListResponseDto(
        List<StarbucksRouteResponseDto> routes
) {
}