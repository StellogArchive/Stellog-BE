package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteResDto(
        Long id,
        String name,
        boolean isOwner,
        int bookmarkCount,
        List<StarbucksInfoResDto> starbucksList
) {
}
