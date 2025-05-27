package org.example.stellog.room.api.dto.response;

public record RoomResDto(
        Long id,
        String name,
        int memberCount,
        long visitedStarbucksCount
) {
}
