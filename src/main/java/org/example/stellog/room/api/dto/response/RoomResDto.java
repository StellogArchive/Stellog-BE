package org.example.stellog.room.api.dto.response;

public record RoomResDto(
        Long roomId,
        String roomName,
        int memberCount,
        long visitedStarbucksCount
) {
}
