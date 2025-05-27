package org.example.stellog.room.api.dto.response;

import java.util.List;

public record RoomListResDto(
        List<RoomResDto> rooms
) {
}
