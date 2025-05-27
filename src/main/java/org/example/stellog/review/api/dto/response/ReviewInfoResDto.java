package org.example.stellog.review.api.dto.response;

import lombok.Builder;

@Builder
public record ReviewInfoResDto(
        Long id,
        Long starbucksId,
        String title,
        String content,
        String createdAt, // createAt
        String visitedAt, // visitedAt
        String author,
        String mainImgUrl,
        boolean isAuthor,
        boolean isLike,
        int likeCount
) {
}