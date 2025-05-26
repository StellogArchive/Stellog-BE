package org.example.stellog.review.api.dto.response;

import lombok.Builder;

@Builder
public record ReviewInfoResDto(
        Long id,
        Long starbucksId,
        String title,
        String content,
        String author,
        boolean isAuthor,
//        String date // createAt
        boolean isLike,
        int likeCount
) {
}