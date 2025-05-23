package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteResDto(
        String routeName,
        List<StarbucksInfoResDto> starbucksList
) {
}
