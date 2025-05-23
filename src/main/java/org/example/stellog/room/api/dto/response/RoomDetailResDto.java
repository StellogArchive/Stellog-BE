package org.example.stellog.room.api.dto.response;

import java.util.List;

public record RoomDetailResDto(
        Long roomId,
        String roomName,
        List<MemberInfoDto> roomMembers
) {
    public record MemberInfoDto(
            Long id,
            String name
    ) {
    }
}
