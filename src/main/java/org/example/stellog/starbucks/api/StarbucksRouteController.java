package org.example.stellog.starbucks.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.starbucks.api.dto.request.StarbucksRouteRequestDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteListResponseDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteResponseDto;
import org.example.stellog.starbucks.application.StarbucksRouteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "StarbucksRoute", description = "스타벅스 최적화 동선 관련 API")
@RequestMapping("/api/v1/routes")
public class StarbucksRouteController {
    private final StarbucksRouteService starbucksRouteService;

    @Operation(
            summary = "스타벅스 최적화 동선 생성",
            description = "주어진 스타벅스 ID 목록을 기반으로 최적화 동선을 계산하여 저장합니다."
    )
    @PostMapping("/optimize/{roomId}")
    public RspTemplate<String> createRoute(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody StarbucksRouteRequestDto starbucksRouteRequestDto) {
        Long routeId = starbucksRouteService.createOptimizedRoute(email, roomId, starbucksRouteRequestDto);
        return new RspTemplate<>(HttpStatus.CREATED, "최적화 동선이 저장되었습니다.", routeId.toString());
    }

    @Operation(
            summary = "방 별 스타벅스 최적화 동선 리스트 조회",
            description = "방 별 스타벅스 최적화 동선 리스트를 조회합니다."
    )
    @GetMapping("room/{roomId}")
    public RspTemplate<StarbucksRouteListResponseDto> getRoute(@AuthenticatedEmail String email, @PathVariable Long roomId) {
        StarbucksRouteListResponseDto route = starbucksRouteService.getRouteStarbucksIds(email, roomId);
        return new RspTemplate<>(HttpStatus.OK, "스타벅스 최적화 동선을 성공적으로 조회하였습니다.", route);
    }

    @Operation(
            summary = "스타벅스 최적화 동선 상세 조회",
            description = "주어진 스타벅스 최적화 동선 상세 정보를 조회합니다."
    )
    @GetMapping("route/{routeId}")
    public RspTemplate<StarbucksRouteResponseDto> getRouteById(@AuthenticatedEmail String email, @PathVariable Long routeId) {
        StarbucksRouteResponseDto route = starbucksRouteService.getRouteById(email, routeId);
        return new RspTemplate<>(HttpStatus.OK, "스타벅스 최적화 동선을 성공적으로 조회하였습니다.", route);
    }

    @Operation(
            summary = "스타벅스 최적화 동선 목록 수정",
            description = "주어진 스타벅스 최적화 동선 목록을 수정합니다."
    )
    @PutMapping("{routeId}")
    public RspTemplate<Void> updateRoute(@AuthenticatedEmail String email, @PathVariable Long routeId, @RequestBody StarbucksRouteRequestDto starbucksRouteRequestDto) {
        starbucksRouteService.updateRoute(email, routeId, starbucksRouteRequestDto);
        return new RspTemplate<>(HttpStatus.OK, "스타벅스 최적화 동선이 성공적으로 수정되었습니다.");
    }

    @Operation(
            summary = "스타벅스 최적화 동선 삭제",
            description = "주어진 스타벅스 최적화 동선을 삭제합니다."
    )
    @DeleteMapping("{routeId}")
    public RspTemplate<Void> deleteRoute(@AuthenticatedEmail String email, @PathVariable Long routeId) {
        starbucksRouteService.deleteRoute(email, routeId);
        return new RspTemplate<>(HttpStatus.NO_CONTENT, "스타벅스 최적화 동선이 성공적으로 삭제되었습니다.");
    }
}
