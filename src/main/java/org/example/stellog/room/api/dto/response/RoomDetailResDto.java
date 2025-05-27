package org.example.stellog.room.api.dto.response;

import java.util.List;

public record RoomDetailResDto(
        Long id,
        String name,
        boolean isOwner,
        List<MemberInfoDto> roomMembers,
        int visitedStarbucksCount,
        List<ReviewInfoDto> reviews
) {
    public record MemberInfoDto(
            Long id,
            String name
    ) {
    }

    public record ReviewInfoDto(
            Long id,
            String title,
            String date,
            String starbucksName
    ) {
    }
}
