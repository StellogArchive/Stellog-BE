package org.example.stellog.review.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewListResDto(
        List<ReviewInfoResDto> reviews
) {
}