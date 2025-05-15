package org.example.stellog.review.api.dto.respoonse;

import lombok.Builder;

@Builder
public record ReviewResponseDto(
        Long reviewId,
        Long starbucksId,
        String title,
        String content
//        String author,
//        String date // createAt
) {
}