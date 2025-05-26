package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteResDto(
        String name,
        boolean isAuthor,
        List<StarbucksInfoResDto> starbucksList
) {
}
