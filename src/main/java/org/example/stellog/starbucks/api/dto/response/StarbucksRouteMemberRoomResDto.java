package org.example.stellog.starbucks.api.dto.response;

import java.util.List;

public record StarbucksRouteMemberRoomResDto(
        Long roomId,
        String roomName,
        List<StarbucksRouteResDto> routes
) {
}
