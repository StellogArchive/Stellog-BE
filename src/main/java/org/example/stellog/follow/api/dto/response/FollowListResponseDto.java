package org.example.stellog.follow.api.dto.response;

import java.util.List;

public record FollowListResponseDto(
        List<FollowResponseDto> followList
) {
}
