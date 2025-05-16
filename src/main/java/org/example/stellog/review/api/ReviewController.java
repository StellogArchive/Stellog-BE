package org.example.stellog.review.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.review.api.dto.request.ReviewRequestDto;
import org.example.stellog.review.api.dto.response.ReviewListResponseDto;
import org.example.stellog.review.api.dto.response.ReviewResponseDto;
import org.example.stellog.review.application.ReviewService;
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
                reviewService.getAllReviewsByStarbucksId(email, starbucksId));
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
    public RspTemplate<ReviewResponseDto> getReviewById(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰를 성공적으로 조회하였습니다.",
                reviewService.getReviewById(email, reviewId));
    }

    @Operation(
            summary = "리뷰 수정",
            description = "리뷰를 수정합니다. 리뷰를 작성한 사용자만 수정할 수 있습니다."
    )
    @PutMapping({"/{reviewId}"})
    public RspTemplate<Void> updateReview(@AuthenticatedEmail String email, @PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.updateReview(email, reviewId, reviewRequestDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰가 성공적으로 수정되었습니다.");
    }

    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰를 삭제합니다. 리뷰를 작성한 사용자만 삭제할 수 있습니다."
    )
    @DeleteMapping("/{reviewId}")
    public RspTemplate<Void> deleteReview(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        reviewService.deleteReview(email, reviewId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰가 성공적으로 삭제되었습니다.");
    }

    @Operation(
            summary = "리뷰 좋아요",
            description = "리뷰에 좋아요를 누릅니다."
    )
    @PostMapping("/like/{reviewId}")
    public RspTemplate<Void> likeReview(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        reviewService.likeReview(email, reviewId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰에 좋아요를 눌렀습니다.");
    }

    @Operation(
            summary = "리뷰 좋아요 취소",
            description = "리뷰에 좋아요를 취소합니다."
    )
    @DeleteMapping("/like/{reviewId}")
    public RspTemplate<Void> unlikeReview(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        reviewService.unlikeReview(email, reviewId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰에 좋아요를 취소했습니다.");
    }
}
