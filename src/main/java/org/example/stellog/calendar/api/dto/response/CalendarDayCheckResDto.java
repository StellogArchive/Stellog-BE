package org.example.stellog.calendar.api.dto.response;

public record CalendarDayCheckResDto(
        String date,
        boolean hasCalendar,
        boolean hasReview
) {
}