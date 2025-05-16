package org.example.stellog.review.api.dto.request;

public record ReviewRequestDto(
        String title,
        String content,
        Long starbucksId
) {
}
