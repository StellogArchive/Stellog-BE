package org.example.stellog.review.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.review.api.dto.request.ReviewReqDto;
import org.example.stellog.review.api.dto.response.ReviewInfoResDto;
import org.example.stellog.review.api.dto.response.ReviewListResDto;
import org.example.stellog.review.application.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController implements ReviewControllerDocs {
    private final ReviewService reviewService;

    @PostMapping("{roomId}")
    public RspTemplate<Void> createReview(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody ReviewReqDto reviewReqDto) {
        reviewService.createReview(email, roomId, reviewReqDto);
        System.out.println(reviewReqDto.starbucksId());
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "리뷰가 성공적으로 생성되었습니다.");
    }

    @GetMapping("/starbucks/{starbucksId}")
    public RspTemplate<ReviewListResDto> getAllReviewsByStarbucksId(@AuthenticatedEmail String email, @PathVariable Long starbucksId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getAllReviewsByStarbucksId(email, starbucksId));
    }

    @GetMapping("/room/{roomId}")
    public RspTemplate<ReviewListResDto> getAllReviewsByRoomId(@AuthenticatedEmail String email, @PathVariable Long roomId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getAllReviewsByRoomId(email, roomId));
    }

    @GetMapping("{roomId}/{starbucksId}")
    public RspTemplate<ReviewListResDto> getReviewsByRoomIdAndStarbucksId(@AuthenticatedEmail String email, @PathVariable Long roomId, @PathVariable Long starbucksId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰 목록을 성공적으로 조회하였습니다.",
                reviewService.getReviewsByRoomIdAndStarbucksId(email, roomId, starbucksId));
    }

    @GetMapping("/{reviewId}")
    public RspTemplate<ReviewInfoResDto> getReviewById(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰를 성공적으로 조회하였습니다.",
                reviewService.getReviewById(email, reviewId));
    }

    @PutMapping({"/{reviewId}"})
    public RspTemplate<Void> updateReview(@AuthenticatedEmail String email, @PathVariable Long reviewId, @RequestBody ReviewReqDto reviewReqDto) {
        reviewService.updateReview(email, reviewId, reviewReqDto);
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

    @PostMapping("/like/{reviewId}")
    public RspTemplate<Void> likeReview(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        reviewService.likeReview(email, reviewId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰에 좋아요를 눌렀습니다.");
    }

    @DeleteMapping("/like/{reviewId}")
    public RspTemplate<Void> unlikeReview(@AuthenticatedEmail String email, @PathVariable Long reviewId) {
        reviewService.unlikeReview(email, reviewId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "리뷰에 좋아요를 취소했습니다.");
    }
}
