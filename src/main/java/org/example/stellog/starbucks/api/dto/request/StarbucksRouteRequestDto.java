package org.example.stellog.starbucks.api.dto.request;

import java.util.List;

public record StarbucksRouteRequestDto(
        String name,
        List<Long> starbucksIds
) {
}
