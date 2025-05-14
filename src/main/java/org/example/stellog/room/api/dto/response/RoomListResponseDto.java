package org.example.stellog.room.api.dto.response;

import java.util.List;

public record RoomListResponseDto(
        List<RoomResponseDto> roomList
) {
}
