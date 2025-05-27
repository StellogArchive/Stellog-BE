package org.example.stellog.review.api.dto.request;

public record ReviewReqDto(
        String title,
        String content,
        String visitedAt,
        Long starbucksId,
        String mainImgUrl
) {
}
