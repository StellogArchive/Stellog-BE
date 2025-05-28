package org.example.stellog.starbucks.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.starbucks.api.dto.request.StarbucksRouteReqDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteListResDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteResDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "StarbucksRoute", description = "스타벅스 최적화 동선 관련 API")
public interface StarbucksRouteControllerDocs {
    @Operation(
            summary = "스타벅스 최적화 동선 생성",
            description = "주어진 스타벅스 ID 목록을 기반으로 최적화 동선을 계산하여 저장합니다."
    )
    RspTemplate<String> createRoute(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto);

    @Operation(
            summary = "방 별 스타벅스 최적화 동선 리스트 조회",
            description = "방 별 스타벅스 최적화 동선 리스트를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StarbucksRouteListResDto.class)
            )
    )
    RspTemplate<StarbucksRouteListResDto> getRouteByRoomId(@AuthenticatedEmail String email, @PathVariable Long roomId);

    @Operation(
            summary = "스타벅스 최적화 동선 상세 조회",
            description = "주어진 스타벅스 최적화 동선 상세 정보를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StarbucksRouteResDto.class)
            )
    )
    RspTemplate<StarbucksRouteResDto> getRouteDetail(@AuthenticatedEmail String email, @PathVariable Long routeId);

    @Operation(
            summary = "스타벅스 최적화 동선 목록 수정",
            description = "주어진 스타벅스 최적화 동선 목록을 수정합니다."
    )
    RspTemplate<Void> updateRoute(@AuthenticatedEmail String email, @PathVariable Long routeId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto);

    @Operation(
            summary = "스타벅스 최적화 동선 삭제",
            description = "주어진 스타벅스 최적화 동선을 삭제합니다."
    )
    RspTemplate<Void> deleteRoute(@AuthenticatedEmail String email, @PathVariable Long routeId);

    @Operation(
            summary = "스타벅스 최적화 동선 좋아요 추가",
            description = "주어진 스타벅스 최적화 동선에 좋아요를 추가합니다."
    )
    RspTemplate<Void> likeStarbucksRoute(@AuthenticatedEmail String email, @PathVariable Long routeId);

    @Operation(
            summary = "스타벅스 최적화 동선 좋아요 취소",
            description = "주어진 스타벅스 최적화 동선에 좋아요를 취소합니다."
    )
    RspTemplate<Void> unlikeStarbucksRoute(@AuthenticatedEmail String email, @PathVariable Long routeId);
}
