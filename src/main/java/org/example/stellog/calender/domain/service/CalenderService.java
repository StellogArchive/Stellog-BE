package org.example.stellog.calender.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.stellog.calender.api.dto.request.CalenderReqDto;
import org.example.stellog.calender.api.dto.response.CalenderInfoResDto;
import org.example.stellog.calender.api.dto.response.CalenderListResDto;
import org.example.stellog.calender.api.dto.response.CalenderStarbucksResDto;
import org.example.stellog.calender.domain.Calender;
import org.example.stellog.calender.domain.repository.CalenderRepository;
import org.example.stellog.calender.exception.CalenderNotFoundException;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.StarbucksReview;
import org.example.stellog.review.domain.repository.ReviewRepository;
import org.example.stellog.review.domain.repository.StarbucksReviewRepository;
import org.example.stellog.room.domain.Room;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalenderService {
    private final CalenderRepository calenderRepository;
    private final MemberRoomService memberRoomService;
    private final ReviewRepository reviewRepository;
    private final StarbucksReviewRepository starbucksReviewRepository;

    @Transactional
    public void createCalender(String email, Long roomId, CalenderReqDto calenderReqDto) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        Calender calender = Calender.builder()
                .name(calenderReqDto.name())
                .date(calenderReqDto.date())
                .completed(false)
                .member(member)
                .room(room)
                .build();
        calenderRepository.save(calender);
    }

    @Transactional
    public void completeCalender(String email, Long roomId, Long calenderId) {
        Calender calender = findCalenderAndValidateMemberAndRoom(email, roomId, calenderId);

        calender.completed();
    }

    @Transactional
    public void cancelCompleteCalender(String email, Long roomId, Long calenderId) {
        Calender calender = findCalenderAndValidateMemberAndRoom(email, roomId, calenderId);
        calender.cancelCompleted();
    }

    public CalenderListResDto getCalenderByDate(String email, Long roomId, String date) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        List<Calender> calenders = calenderRepository.findAllByDateAndMemberAndRoom(date, member, room);
        List<Review> reviews = reviewRepository.findAllByVisitedAtAndRoom(date, room);

        List<CalenderInfoResDto> calenderDtos = toCalenderDtos(calenders);
        List<CalenderStarbucksResDto> reviewDtos = toReviewDtos(reviews);

        return new CalenderListResDto(date, calenderDtos, reviewDtos);
    }

    @Transactional
    public void updateCalender(String email, Long roomId, Long calenderId, CalenderReqDto calenderReqDto) {
        Calender calender = findCalenderAndValidateMemberAndRoom(email, roomId, calenderId);
        calender.updateCalender(calenderReqDto.name(), calenderReqDto.date());
    }

    @Transactional
    public void deleteCalender(String email, Long roomId, Long calenderId) {
        Calender calender = findCalenderAndValidateMemberAndRoom(email, roomId, calenderId);
        calenderRepository.delete(calender);
    }

    private Calender findCalenderAndValidateMemberAndRoom(String email, Long roomId, Long calenderId) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        return findCalenderById(calenderId);
    }

    private Calender findCalenderById(Long calenderId) {
        return calenderRepository.findById(calenderId)
                .orElseThrow(() -> new CalenderNotFoundException("해당 캘린더를 찾을 수 없습니다. id: " + calenderId));
    }

    private List<CalenderInfoResDto> toCalenderDtos(List<Calender> calenders) {
        return calenders.stream()
                .map(c -> CalenderInfoResDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .completed(c.isCompleted())
                        .build())
                .toList();
    }

    private List<CalenderStarbucksResDto> toReviewDtos(List<Review> reviews) {
        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByReviewIn(reviews);
        return starbucksReviews.stream()
                .map(sr -> new CalenderStarbucksResDto(sr.getReview().getId(), sr.getStarbucks().getName()))
                .toList();
    }
}
