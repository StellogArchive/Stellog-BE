package org.example.stellog.calendar.api.dto.response;

import lombok.Builder;

@Builder
public record CalendarInfoResDto(
        Long id,
        String name,
        boolean completed
) {
}
