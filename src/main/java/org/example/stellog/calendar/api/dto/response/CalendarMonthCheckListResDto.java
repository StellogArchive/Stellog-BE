package org.example.stellog.calendar.api.dto.response;

import java.util.List;

public record CalendarMonthCheckListResDto(
        String yearMonth,
        List<CalendarDayCheckResDto> days
) {
}