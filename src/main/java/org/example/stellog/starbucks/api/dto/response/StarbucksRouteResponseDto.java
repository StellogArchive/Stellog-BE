package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteResponseDto(
        String name,
        List<Long> starbucksIds
) {
}
