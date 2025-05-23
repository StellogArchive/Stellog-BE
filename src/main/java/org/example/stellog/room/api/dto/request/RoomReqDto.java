package org.example.stellog.room.api.dto.request;

import java.util.List;

public record RoomReqDto(
        String name,
        boolean isPublic,
        List<Long> memberIdList
) {
}
