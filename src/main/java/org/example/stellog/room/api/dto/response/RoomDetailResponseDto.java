package org.example.stellog.room.api.dto.response;

import java.util.List;

public record RoomDetailResponseDto(
        Long roomId,
        String roomName,
        List<MemberSimpleDto> roomMembers
) {
}
