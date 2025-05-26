package org.example.stellog.review.api.dto.response;

import lombok.Builder;

@Builder
public record ReviewInfoResDto(
        Long id,
        Long starbucksId,
        String title,
        String content,
        String author,
        String mainImgUrl,
        String date, // createAt
        boolean isAuthor,
        boolean isLike,
        int likeCount
) {
}