package org.example.stellog.calender.api.dto.response;

import lombok.Builder;

@Builder
public record CalenderInfoResDto(
        Long id,
        String name,
        boolean completed
) {
}
