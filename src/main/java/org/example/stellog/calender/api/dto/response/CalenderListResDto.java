package org.example.stellog.calender.api.dto.response;

import java.util.List;

public record CalenderListResDto(
        String date,
        List<CalenderInfoResDto> calenders,
        List<CalenderStarbucksResDto> starbucksCalenders
) {
}
