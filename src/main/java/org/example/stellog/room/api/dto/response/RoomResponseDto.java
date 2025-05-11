package org.example.stellog.room.api.dto.response;

public record RoomResponseDto(
        Long roomId,
        String roomName,
        int memberCount
) {
}
