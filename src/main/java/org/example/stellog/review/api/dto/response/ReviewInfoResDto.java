package org.example.stellog.review.api.dto.response;

import lombok.Builder;

@Builder
public record ReviewInfoResDto(
        Long reviewId,
        Long starbucksId,
        String title,
        String content,
        String author,
//        String date // createAt
        boolean isLike,
        int likeCount
) {
}