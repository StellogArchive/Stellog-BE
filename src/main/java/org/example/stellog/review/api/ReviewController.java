package org.example.stellog.review.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.review.api.dto.request.ReviewRequestDto;
import org.example.stellog.review.api.dto.respoonse.ReviewListResponseDto;
import org.example.stellog.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("{roomId}")
    public RspTemplate<Void> createReview(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.createReview(email, roomId, reviewRequestDto);
        System.out.println(reviewRequestDto.starbucksId());
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "리뷰가 성공적으로 생성되었습니다.");
    }

    @GetMapping("{starbucksId}")
    public RspTemplate<ReviewListResponseDto> getAllReviewsByStarbucksId(@AuthenticatedEmail String email, @PathVariable Long starbucksId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getAllReviewsByStarbucksId(starbucksId));
    }

    @GetMapping("/room/{roomId}")
    public RspTemplate<ReviewListResponseDto> getAllReviewsByRoomId(@AuthenticatedEmail String email, @PathVariable Long roomId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getAllReviewsByRoomId(roomId));
    }
}
