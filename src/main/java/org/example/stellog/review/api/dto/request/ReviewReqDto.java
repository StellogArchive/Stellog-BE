package org.example.stellog.review.api.dto.request;

public record ReviewReqDto(
        String title,
        String content,
        Long starbucksId
) {
}
