package org.example.stellog.review.api.dto.respoonse;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewListResponseDto(
        List<ReviewResponseDto> reviewList
) {
}