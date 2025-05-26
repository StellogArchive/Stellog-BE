package org.example.stellog.starbucks.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.starbucks.api.dto.request.StarbucksRouteReqDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteListResDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteResDto;
import org.example.stellog.starbucks.application.StarbucksRouteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routes")
public class StarbucksRouteController implements StarbucksRouteControllerDocs {
    private final StarbucksRouteService starbucksRouteService;

    @PostMapping("/optimize/{roomId}")
    public RspTemplate<String> createRoute(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto) {
        Long routeId = starbucksRouteService.createOptimizedRoute(email, roomId, starbucksRouteReqDto);
        return new RspTemplate<>(HttpStatus.CREATED, "최적화 동선이 저장되었습니다.", routeId.toString());
    }

    @GetMapping("room/{roomId}")
    public RspTemplate<StarbucksRouteListResDto> getRouteByRoomId(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId) {
        StarbucksRouteListResDto route = starbucksRouteService.getRouteByRoomId(email, roomId);
        return new RspTemplate<>(HttpStatus.OK, "방 별 스타벅스 최적화 동선을 성공적으로 조회하였습니다.", route);
    }

    @GetMapping("route/{routeId}")
    public RspTemplate<StarbucksRouteResDto> getRouteDetail(@AuthenticatedEmail String email, @PathVariable(value = "routeId") Long routeId) {
        StarbucksRouteResDto route = starbucksRouteService.getRouteDetail(email, routeId);
        return new RspTemplate<>(HttpStatus.OK, "스타벅스 최적화 동선 상세 조회를 성공적으로 조회하였습니다.", route);
    }

    @PutMapping("{routeId}")
    public RspTemplate<Void> updateRoute(@AuthenticatedEmail String email, @PathVariable(value = "routeId") Long routeId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto) {
        starbucksRouteService.updateRoute(email, routeId, starbucksRouteReqDto);
        return new RspTemplate<>(HttpStatus.OK, "스타벅스 최적화 동선이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("{routeId}")
    public RspTemplate<Void> deleteRoute(@AuthenticatedEmail String email, @PathVariable(value = "routeId") Long routeId) {
        starbucksRouteService.deleteRoute(email, routeId);
        return new RspTemplate<>(HttpStatus.NO_CONTENT, "스타벅스 최적화 동선이 성공적으로 삭제되었습니다.");
    }
}
