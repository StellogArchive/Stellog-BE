package org.example.stellog.calendar.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.calendar.api.dto.request.CalendarReqDto;
import org.example.stellog.calendar.api.dto.response.CalendarListResDto;
import org.example.stellog.calendar.api.dto.response.CalendarMonthCheckListResDto;
import org.example.stellog.calendar.domain.service.CalendarService;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/calendars")
public class CalendarController implements CalendarControllerDocs {
    private final CalendarService calendarService;

    @PostMapping("/{roomId}")
    public RspTemplate<Void> createCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestBody CalendarReqDto calendarReqDto) {
        calendarService.createCalendar(email, roomId, calendarReqDto);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "캘린더가 성공적으로 생성되었습니다.");
    }

    @PostMapping("/complete/{roomId}/{calendarId}")
    public RspTemplate<Void> completeCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId) {
        calendarService.completeCalendar(email, roomId, calendarId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 일정이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/cancel-complete/{roomId}/{calendarId}")
    public RspTemplate<Void> cancelCompleteCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId) {
        calendarService.cancelCompleteCalendar(email, roomId, calendarId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 일정 완료가 성공적으로 취소되었습니다.");
    }

    @GetMapping("/{roomId}")
    public RspTemplate<CalendarListResDto> getCalendarByDate(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestParam(name = "date") String date) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 스타벅스 날짜 조회를 성공적으로 조회하였습니다.",
                calendarService.getCalendarByDate(email, roomId, date));
    }

    @GetMapping("/{roomId}/month")
    public RspTemplate<CalendarMonthCheckListResDto> getCalendarStatusByMonth(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestParam(name = "month") String yearMonth) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 스타벅스 월별 조회를 성공적으로 조회하였습니다.",
                calendarService.getCalendarStatusByMonth(email, roomId, yearMonth));
    }

    @PutMapping("/{roomId}/{calendarId}")
    public RspTemplate<Void> updateCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId, @RequestBody CalendarReqDto calendarReqDto) {
        calendarService.updateCalendar(email, roomId, calendarId, calendarReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{roomId}/{calendarId}")
    public RspTemplate<Void> deleteCalendar(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calendarId") Long calendarId) {
        calendarService.deleteCalendar(email, roomId, calendarId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더가 성공적으로 삭제되었습니다.");
    }
}
