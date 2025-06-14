package org.example.stellog.calender.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.calender.api.dto.request.CalenderReqDto;
import org.example.stellog.calender.api.dto.response.CalenderListResDto;
import org.example.stellog.calender.domain.service.CalenderService;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/calenders")
public class CalenderController implements CalenderControllerDocs {
    private final CalenderService calenderService;

    @PostMapping("/{roomId}")
    public RspTemplate<Void> createCalender(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestBody CalenderReqDto calenderReqDto) {
        calenderService.createCalender(email, roomId, calenderReqDto);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "캘린더가 성공적으로 생성되었습니다.");
    }

    @PostMapping("/complete/{roomId}/{calenderId}")
    public RspTemplate<Void> completeCalender(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calenderId") Long calenderId) {
        calenderService.completeCalender(email, roomId, calenderId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 일정이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/cancel-complete/{roomId}/{calenderId}")
    public RspTemplate<Void> cancelCompleteCalender(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calenderId") Long calenderId) {
        calenderService.cancelCompleteCalender(email, roomId, calenderId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 일정 완료가 성공적으로 취소되었습니다.");
    }

    @GetMapping("/{roomId}")
    public RspTemplate<CalenderListResDto> getCalenderByDate(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestParam(name = "date") String date) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더 스타벅스 날짜 조회를 성공적으로 조회하였습니다.",
                calenderService.getCalenderByDate(email, roomId, date));
    }

    @PutMapping("/{roomId}/{calenderId}")
    public RspTemplate<Void> updateCalender(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calenderId") Long calenderId, @RequestBody CalenderReqDto calenderReqDto) {
        calenderService.updateCalender(email, roomId, calenderId, calenderReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{roomId}/{calenderId}")
    public RspTemplate<Void> deleteCalender(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @PathVariable(value = "calenderId") Long calenderId) {
        calenderService.deleteCalender(email, roomId, calenderId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "캘린더가 성공적으로 삭제되었습니다.");
    }
}
