package org.example.stellog.review.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.review.api.dto.request.ReviewRequestDto;
import org.example.stellog.review.api.dto.respoonse.ReviewListResponseDto;
import org.example.stellog.review.api.dto.respoonse.ReviewResponseDto;
import org.example.stellog.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 생성",
            description = "리뷰를 생성합니다."
    )
    @PostMapping("{roomId}")
    public RspTemplate<Void> createReview(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.createReview(email, roomId, reviewRequestDto);
        System.out.println(reviewRequestDto.starbucksId());
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "리뷰가 성공적으로 생성되었습니다.");
    }

    @Operation(
            summary = "스타벅스 별 리뷰 목록 조회",
            description = "스타벅스 별 리뷰 목록을 조회합니다."
    )
    @GetMapping("/starbucks/{starbucksId}")
    public RspTemplate<ReviewListResponseDto> getAllReviewsByStarbucksId(@AuthenticatedEmail String email, @PathVariable Long starbucksId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getAllReviewsByStarbucksId(starbucksId));
    }

    @Operation(
            summary = "방 별 리뷰 목록 조회",
            description = "방 별 리뷰 목록을 조회합니다."
    )
    @GetMapping("/room/{roomId}")
    public RspTemplate<ReviewListResponseDto> getAllReviewsByRoomId(@AuthenticatedEmail String email, @PathVariable Long roomId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getAllReviewsByRoomId(email, roomId));
    }

    @Operation(
            summary = "리뷰 상세 조회",
            description = "리뷰를 상세 조회합니다."
    )
    @GetMapping("/{reviewId}")
    public RspTemplate<ReviewResponseDto> getReviewById(@PathVariable Long reviewId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰를 성공적으로 조회하였습니다.",
                reviewService.getReviewById(reviewId));
    }

    @PutMapping({"/{reviewId}"})
    public RspTemplate<Void> updateReview(@AuthenticatedEmail String email, @PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.updateReview(email, reviewId, reviewRequestDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{reviewId}")
    public RspTemplate<Void> deleteReview(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        reviewService.deleteReview(email, reviewId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰가 성공적으로 삭제되었습니다.");
    }
}
