package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteListResDto(
        List<StarbucksRouteResDto> routes
) {
}