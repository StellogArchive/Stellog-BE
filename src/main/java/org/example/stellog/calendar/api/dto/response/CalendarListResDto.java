package org.example.stellog.calendar.api.dto.response;

import java.util.List;

public record CalendarListResDto(
        String date,
        List<CalendarInfoResDto> calendars,
        List<CalendarStarbucksResDto> starbucksCalendars
) {
}
