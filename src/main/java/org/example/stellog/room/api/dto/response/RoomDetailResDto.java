package org.example.stellog.room.api.dto.response;

import java.util.List;

public record RoomDetailResDto(
        Long id,
        String name,
        boolean isOwner,
        List<MemberInfoDto> roomMembers
) {
    public record MemberInfoDto(
            Long id,
            String name
    ) {
    }
}
