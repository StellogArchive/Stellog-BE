package org.example.stellog.calendar.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.calendar.api.dto.request.CalendarReqDto;
import org.example.stellog.calendar.api.dto.response.CalendarListResDto;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Calendar", description = "캘린더 관련 API")
public interface CalendarControllerDocs {
    @Operation(
            summary = "캘린더 일정 생성",
            description = "캘린더 일정을 생성합니다."
    )
    public RspTemplate<Void> createCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestBody CalendarReqDto calendarReqDto);

    @Operation(
            summary = "캘린더 일정 완료",
            description = "캘린더 일정 정보를 완료합니다."
    )
    public RspTemplate<Void> completeCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId);

    @Operation(
            summary = "캘린더 일정 완료 취소",
            description = "캘린더 일정 정보를 완료 취소합니다."
    )
    public RspTemplate<Void> cancelCompleteCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId);

    @Operation(
            summary = "날짜 별 캘린더 조회",
            description = "날짜 별로 캘린더 정보(스타벅스 리뷰, 일정)를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CalendarListResDto.class)
            )
    )
    public RspTemplate<CalendarListResDto> getCalendarByDate(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestParam(name = "date") String date);

    @Operation(
            summary = "캘린더 일정 수정",
            description = "캘린더 일정 정보를 수정합니다."
    )
    public RspTemplate<Void> updateCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId, @RequestBody CalendarReqDto calendarReqDto);

    @Operation(
            summary = "캘린더 삭제",
            description = "캘린더 일정 정보를 삭제합니다."
    )
    public RspTemplate<Void> deleteCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId);
}
