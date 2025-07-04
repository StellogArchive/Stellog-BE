package org.example.stellog.starbucks.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.starbucks.api.dto.request.StarbucksRouteReqDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksListRouteMemberRoomResDto;
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
    public RspTemplate<Void> createRoute(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId, @RequestBody StarbucksRouteReqDto starbucksRouteReqDto) {
        starbucksRouteService.createOptimizedRoute(email, roomId, starbucksRouteReqDto);
        return new RspTemplate<>(HttpStatus.CREATED, "최적화 동선이 저장되었습니다.");
    }

    @GetMapping("room/{roomId}")
    public RspTemplate<StarbucksRouteListResDto> getRouteByRoomId(@AuthenticatedEmail String email, @PathVariable(value = "roomId") Long roomId) {
        StarbucksRouteListResDto route = starbucksRouteService.getRouteByRoomId(email, roomId);
        return new RspTemplate<>(HttpStatus.OK, "방 별 스타벅스 최적화 동선을 성공적으로 조회하였습니다.", route);
    }

    @GetMapping("/member")
    public RspTemplate<StarbucksListRouteMemberRoomResDto> getRoutesByCurrentMember(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK, "사용자가 생성한 최적화 동선을 성공적으로 조회하였습니다.", starbucksRouteService.getRoutesByCurrentMember(email));
    }

    @GetMapping("/bookmark")
    public RspTemplate<StarbucksRouteListResDto> getBookmarkedRoutes(@AuthenticatedEmail String email) {
        StarbucksRouteListResDto bookmarkedRoutes = starbucksRouteService.getBookmarkedRoutes(email);
        return new RspTemplate<>(HttpStatus.OK, "북마크된 스타벅스 최적화 동선을 성공적으로 조회하였습니다.", bookmarkedRoutes);
    }

    @GetMapping("/popular")
    public RspTemplate<StarbucksRouteListResDto> getPopularRoutes(@AuthenticatedEmail String email) {
        StarbucksRouteListResDto popularRoutes = starbucksRouteService.getPopularRoutes(email);
        return new RspTemplate<>(HttpStatus.OK, "인기 있는 스타벅스 최적화 동선을 성공적으로 조회하였습니다.", popularRoutes);
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

    @PostMapping("/bookmark/{routeId}")
    public RspTemplate<Void> saveStarbucksRouteBookmark(@AuthenticatedEmail String email, @PathVariable(value = "routeId") Long routeId) {
        starbucksRouteService.saveStarbucksRouteBookmark(email, routeId);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "스타벅스 최적화 동선을 북마크에 추가하였습니다.");
    }

    @DeleteMapping("/bookmark/{routeId}")
    public RspTemplate<Void> deleteStarbucksRouteBookmark(@AuthenticatedEmail String email, @PathVariable(value = "routeId") Long routeId) {
        starbucksRouteService.deleteStarbucksRouteBookmark(email, routeId);
        return new RspTemplate<>(
                HttpStatus.NO_CONTENT,
                "스타벅스 최적화 동선을 북마크에서 삭제하였습니다.");
    }
}
