package org.example.stellog.starbucks.api.dto.request;

import java.util.List;

public record StarbucksRouteReqDto(
        String name,
        List<Long> starbucksIds
) {
}
