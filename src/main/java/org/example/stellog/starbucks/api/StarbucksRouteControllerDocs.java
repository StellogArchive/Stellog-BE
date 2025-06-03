package org.example.stellog.starbucks.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.starbucks.api.dto.request.StarbucksRouteReqDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksListRouteMemberRoomResDto;
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
    RspTemplate<Void> createRoute(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto);

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
            summary = "사용자가 생성한 스타벅스 최적화 동선 조회",
            description = "현재 로그인 한 사용자가 생성한 스타벅스 최적화 동선을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StarbucksListRouteMemberRoomResDto.class)
            )
    )
    RspTemplate<StarbucksListRouteMemberRoomResDto> getRoutesByCurrentMember(@AuthenticatedEmail String email);

    @Operation(
            summary = "북마크한 스타벅스 최적화 동선 조회",
            description = "현재 로그인 한 사용자가 북마크한 스타벅스 최적화 동선을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StarbucksRouteListResDto.class)
            )
    )
    RspTemplate<StarbucksRouteListResDto> getBookmarkedRoutes(@AuthenticatedEmail String email);

    @Operation(
            summary = "인기 있는 스타벅스 최적화 동선 조회",
            description = "저장 수가 많은 순서로 스타벅스 최적화 동선을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StarbucksRouteListResDto.class)
            )
    )
    RspTemplate<StarbucksRouteListResDto> getPopularRoutes(@AuthenticatedEmail String email);

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
            summary = "최적화 동선의 스타벅스 목록 수정",
            description = "최적화 동선에 포함된 스타벅스 목록을 수정합니다."
    )
    RspTemplate<Void> updateRoute(@AuthenticatedEmail String email, @PathVariable Long routeId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto);

    @Operation(
            summary = "스타벅스 최적화 동선 삭제",
            description = "주어진 스타벅스 최적화 동선을 삭제합니다."
    )
    RspTemplate<Void> deleteRoute(@AuthenticatedEmail String email, @PathVariable Long routeId);

    @Operation(
            summary = "스타벅스 최적화 동선 북마크 저장",
            description = "주어진 스타벅스 최적화 동선을 북마크에 저장합니다."
    )
    RspTemplate<Void> saveStarbucksRouteBookmark(@AuthenticatedEmail String email, @PathVariable Long routeId);

    @Operation(
            summary = "스타벅스 최적화 동선 북마크 삭제",
            description = "주어진 스타벅스 최적화 동선을 북마크에서 삭제합니다."
    )
    RspTemplate<Void> deleteStarbucksRouteBookmark(@AuthenticatedEmail String email, @PathVariable Long routeId);
}
