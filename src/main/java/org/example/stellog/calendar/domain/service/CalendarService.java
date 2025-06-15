package org.example.stellog.calendar.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.stellog.calendar.api.dto.request.CalendarReqDto;
import org.example.stellog.calendar.api.dto.response.*;
import org.example.stellog.calendar.domain.Calendar;
import org.example.stellog.calendar.domain.repository.CalendarRepository;
import org.example.stellog.calendar.exception.CalendarNotFoundException;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.StarbucksReview;
import org.example.stellog.review.domain.repository.ReviewRepository;
import org.example.stellog.review.domain.repository.StarbucksReviewRepository;
import org.example.stellog.room.domain.Room;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final MemberRoomService memberRoomService;
    private final ReviewRepository reviewRepository;
    private final StarbucksReviewRepository starbucksReviewRepository;

    @Transactional
    public void createCalendar(String email, Long roomId, CalendarReqDto calendarReqDto) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        Calendar calendar = Calendar.builder()
                .name(calendarReqDto.name())
                .date(calendarReqDto.date())
                .completed(false)
                .member(member)
                .room(room)
                .build();
        calendarRepository.save(calendar);
    }

    @Transactional
    public void completeCalendar(String email, Long roomId, Long calendarId) {
        Calendar calendar = findCalendarAndValidateMemberAndRoom(email, roomId, calendarId);

        calendar.completed();
    }

    @Transactional
    public void cancelCompleteCalendar(String email, Long roomId, Long calendarId) {
        Calendar calendar = findCalendarAndValidateMemberAndRoom(email, roomId, calendarId);
        calendar.cancelCompleted();
    }

    public CalendarListResDto getCalendarByDate(String email, Long roomId, String date) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        List<Calendar> calendars = calendarRepository.findAllByDateAndMemberAndRoom(date, member, room);
        List<Review> reviews = reviewRepository.findAllByVisitedAtAndRoom(date, room);

        List<CalendarInfoResDto> calendarDtos = toCalendarDtos(calendars);
        List<CalendarStarbucksResDto> reviewDtos = toReviewDtos(reviews);

        return new CalendarListResDto(date, calendarDtos, reviewDtos);
    }

    public CalendarMonthCheckListResDto getCalendarStatusByMonth(String email, Long roomId, String yearMonth) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        String datePattern = yearMonth + "%";

        List<Calendar> calendars = calendarRepository.findAllByDateLikeAndMemberAndRoom(datePattern, member, room);
        List<Review> reviews = reviewRepository.findAllByVisitedAtLikeAndRoom(datePattern, room);

        Set<String> calendarDates = calendars.stream()
                .map(Calendar::getDate)
                .collect(Collectors.toSet());

        Set<String> reviewDates = reviews.stream()
                .map(Review::getVisitedAt)
                .collect(Collectors.toSet());

        List<CalendarDayCheckResDto> results = getCalendarDayCheckResDtos(yearMonth, calendarDates, reviewDates);

        return new CalendarMonthCheckListResDto(yearMonth, results);
    }

    @Transactional
    public void updateCalendar(String email, Long roomId, Long calendarId, CalendarReqDto calendarReqDto) {
        Calendar calendar = findCalendarAndValidateMemberAndRoom(email, roomId, calendarId);
        calendar.updateCalendar(calendarReqDto.name(), calendarReqDto.date());
    }

    @Transactional
    public void deleteCalendar(String email, Long roomId, Long calendarId) {
        Calendar calendar = findCalendarAndValidateMemberAndRoom(email, roomId, calendarId);
        calendarRepository.delete(calendar);
    }

    private Calendar findCalendarAndValidateMemberAndRoom(String email, Long roomId, Long calendarId) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        return findCalendarById(calendarId);
    }

    private Calendar findCalendarById(Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new CalendarNotFoundException("해당 캘린더를 찾을 수 없습니다. id: " + calendarId));
    }

    private List<CalendarInfoResDto> toCalendarDtos(List<Calendar> calendars) {
        return calendars.stream()
                .map(c -> CalendarInfoResDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .completed(c.isCompleted())
                        .build())
                .toList();
    }

    private List<CalendarStarbucksResDto> toReviewDtos(List<Review> reviews) {
        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByReviewIn(reviews);
        return starbucksReviews.stream()
                .map(sr -> new CalendarStarbucksResDto(sr.getReview().getId(), sr.getStarbucks().getName()))
                .toList();
    }

    private List<CalendarDayCheckResDto> getCalendarDayCheckResDtos(String yearMonth, Set<String> calendarDates, Set<String> reviewDates) {
        YearMonth ym = YearMonth.parse(yearMonth);
        List<CalendarDayCheckResDto> results = new ArrayList<>();

        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate current = ym.atDay(day);
            String date = current.toString();

            boolean hasCalendar = calendarDates.contains(date);
            boolean hasReview = reviewDates.contains(date);

            results.add(new CalendarDayCheckResDto(date, hasCalendar, hasReview));
        }
        return results;
    }
}
