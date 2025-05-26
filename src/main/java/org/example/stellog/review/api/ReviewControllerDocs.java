package org.example.stellog.review.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.review.api.dto.request.ReviewReqDto;
import org.example.stellog.review.api.dto.response.ReviewInfoResDto;
import org.example.stellog.review.api.dto.response.ReviewListResDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Review", description = "리뷰 관련 API")
public interface ReviewControllerDocs {
    @Operation(
            summary = "리뷰 생성",
            description = "리뷰를 생성합니다."
    )
    RspTemplate<Void> createReview(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody ReviewReqDto reviewReqDto);

    @Operation(
            summary = "스타벅스 별 리뷰 목록 조회",
            description = "스타벅스 별 리뷰 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewListResDto.class)
            )
    )
    RspTemplate<ReviewListResDto> getAllReviewsByStarbucksId(@AuthenticatedEmail String email, @PathVariable Long starbucksId);

    @Operation(
            summary = "방 별 리뷰 목록 조회",
            description = "방 별 리뷰 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewListResDto.class)
            )
    )
    RspTemplate<ReviewListResDto> getAllReviewsByRoomId(@AuthenticatedEmail String email, @PathVariable Long roomId);

    @Operation(
            summary = "방과 스타벅스 별 리뷰 목록 조회",
            description = "방과 스타벅스 별 리뷰 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewListResDto.class)
            )
    )
    RspTemplate<ReviewListResDto> getReviewsByRoomIdAndStarbucksId(@AuthenticatedEmail String email, @PathVariable Long roomId, @PathVariable Long starbucksId);

    @Operation(
            summary = "리뷰 상세 조회",
            description = "리뷰를 상세 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewInfoResDto.class)
            )
    )
    RspTemplate<ReviewInfoResDto> getReviewDetailById(@AuthenticatedEmail String email, @PathVariable Long reviewId);

    @Operation(
            summary = "리뷰 수정",
            description = "리뷰를 수정합니다. 리뷰를 작성한 사용자만 수정할 수 있습니다."
    )
    RspTemplate<Void> updateReview(@AuthenticatedEmail String email, @PathVariable Long reviewId, @RequestBody ReviewReqDto reviewReqDto);

    @Operation(
            summary = "리뷰 삭제",
            description = "리뷰를 삭제합니다. 리뷰를 작성한 사용자만 삭제할 수 있습니다."
    )
    RspTemplate<Void> deleteReview(@AuthenticatedEmail String email, @PathVariable Long reviewId);

    @Operation(
            summary = "리뷰 좋아요",
            description = "리뷰에 좋아요를 누릅니다."
    )
    RspTemplate<Void> likeReview(@AuthenticatedEmail String email, @PathVariable Long reviewId);

    @Operation(
            summary = "리뷰 좋아요 취소",
            description = "리뷰에 좋아요를 취소합니다."
    )
    RspTemplate<Void> unlikeReview(@AuthenticatedEmail String email, @PathVariable Long reviewId);
}
