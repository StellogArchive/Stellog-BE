package org.example.stellog.starbucks.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.example.stellog.room.domain.Room;
import org.example.stellog.starbucks.api.dto.request.StarbucksRouteReqDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksInfoResDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteListResDto;
import org.example.stellog.starbucks.api.dto.response.StarbucksRouteResDto;
import org.example.stellog.starbucks.domain.Starbucks;
import org.example.stellog.starbucks.domain.StarbucksRoute;
import org.example.stellog.starbucks.domain.StarbucksRouteItem;
import org.example.stellog.starbucks.domain.StarbucksRouteLike;
import org.example.stellog.starbucks.domain.repository.StarbucksRepository;
import org.example.stellog.starbucks.domain.repository.StarbucksRouteItemRepository;
import org.example.stellog.starbucks.domain.repository.StarbucksRouteLikeRepository;
import org.example.stellog.starbucks.domain.repository.StarbucksRouteRepository;
import org.example.stellog.starbucks.exception.DuplicateStarbucksRouteLikeException;
import org.example.stellog.starbucks.exception.StarbucksNotFoundException;
import org.example.stellog.starbucks.exception.StarbucksRouteNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StarbucksRouteService {
    private final MemberRoomService memberRoomService;
    private final StarbucksRepository starbucksRepository;
    private final StarbucksRouteRepository starbucksRouteRepository;
    private final StarbucksRouteItemRepository starbucksRouteItemRepository;
    private final StarbucksRouteOptimizer starbucksRouteOptimizer;
    private final StarbucksRouteLikeRepository starbucksRouteLikeRepository;

    @Transactional
    public Long createOptimizedRoute(String email, Long roomId, StarbucksRouteReqDto requestDto) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        List<Starbucks> selected = starbucksRepository.findAllById(requestDto.starbucksIds());
        if (selected.isEmpty()) {
            throw new StarbucksNotFoundException("선택한 스타벅스 지점이 존재하지 않습니다.");
        }

        List<Starbucks> optimalRoute = starbucksRouteOptimizer.findOptimalRoute(selected);
        if (optimalRoute.isEmpty()) {
            throw new StarbucksNotFoundException("최적 경로 계산에 실패했습니다.");
        }

        StarbucksRoute route = starbucksRouteRepository.save(
                StarbucksRoute.builder()
                        .name(requestDto.name())
                        .room(room)
                        .member(member)
                        .build()
        );

        int order = 1;
        for (Starbucks s : optimalRoute) {
            StarbucksRouteItem item = StarbucksRouteItem.builder()
                    .sequenceOrder(order++)
                    .starbucksRoute(route)
                    .starbucks(s)
                    .build();
            starbucksRouteItemRepository.save(item);
        }

        return route.getId();
    }

    public StarbucksRouteListResDto getRouteByRoomId(String email, Long roomId) {
        Member member = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(member, room);

        List<StarbucksRoute> routes = starbucksRouteRepository.findAllByRoom(room);
        if (routes.isEmpty()) {
            throw new StarbucksRouteNotFoundException("해당 방에 연결된 경로가 없습니다. roomId=" + roomId);
        }

        List<StarbucksRouteResDto> routeResponses = new ArrayList<>();
        for (StarbucksRoute route : routes) {
            List<StarbucksRouteItem> items = starbucksRouteItemRepository.findByStarbucksRouteOrderBySequenceOrder(route);
            boolean isOwner = route.getMember().getEmail().equals(email);
            List<StarbucksInfoResDto> starbucksDtos = convertToDtos(items);
            routeResponses.add(new StarbucksRouteResDto(route.getName(), isOwner, starbucksDtos));
        }

        return new StarbucksRouteListResDto(routeResponses);
    }

    public StarbucksRouteResDto getRouteDetail(String email, Long routeId) {
        Member member = memberRoomService.findMemberByEmail(email);
        StarbucksRoute route = findStarbucksRouteById(routeId);
        memberRoomService.validateMemberInRoom(member, route.getRoom());

        List<StarbucksRouteItem> items = starbucksRouteItemRepository.findByStarbucksRouteOrderBySequenceOrder(route);
        boolean isOwner = route.getMember().getEmail().equals(member.getEmail());
        List<StarbucksInfoResDto> starbucksDtos = convertToDtos(items);

        return new StarbucksRouteResDto(route.getName(), isOwner, starbucksDtos);
    }

    @Transactional
    public void updateRoute(String email, Long routeId, StarbucksRouteReqDto dto) {
        Member member = memberRoomService.findMemberByEmail(email);
        StarbucksRoute route = findStarbucksRouteById(routeId);
        memberRoomService.validateMemberInRoom(member, route.getRoom());

        route.updateName(dto.name());

        List<StarbucksRouteItem> existingItems = starbucksRouteItemRepository.findByStarbucksRoute(route);
        Map<Long, StarbucksRouteItem> existingMap = existingItems.stream()
                .collect(Collectors.toMap(item -> item.getStarbucks().getId(), item -> item));

        Set<Long> incomingSet = new HashSet<>(dto.starbucksIds());
        List<StarbucksRouteItem> toRemove = existingItems.stream()
                .filter(item -> !incomingSet.contains(item.getStarbucks().getId()))
                .toList();
        starbucksRouteItemRepository.deleteAll(toRemove);

        for (int i = 0; i < dto.starbucksIds().size(); i++) {
            Long id = dto.starbucksIds().get(i);
            StarbucksRouteItem existingItem = existingMap.get(id);
            if (existingItem == null) {
                Starbucks starbucks = starbucksRepository.findById(id)
                        .orElseThrow(() -> new StarbucksNotFoundException("해당 스타벅스가 존재하지 않습니다. id=" + id));
                StarbucksRouteItem newItem = StarbucksRouteItem.builder()
                        .starbucksRoute(route)
                        .starbucks(starbucks)
                        .sequenceOrder(i)
                        .build();
                starbucksRouteItemRepository.save(newItem);
            } else {
                existingItem.updateSequenceOrder(i);
            }
        }
    }

    @Transactional
    public void deleteRoute(String email, Long routeId) {
        Member member = memberRoomService.findMemberByEmail(email);
        StarbucksRoute route = findStarbucksRouteById(routeId);
        memberRoomService.validateMemberInRoom(member, route.getRoom());

        starbucksRouteItemRepository.deleteAllByStarbucksRoute(route);
        starbucksRouteRepository.delete(route);
    }

    @Transactional
    public void likeStarbucksRoute(String email, Long routeId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        StarbucksRoute route = findStarbucksRouteById(routeId);

        if (starbucksRouteLikeRepository.existsByMemberAndStarbucksRoute(currentMember, route)) {
            throw new DuplicateStarbucksRouteLikeException();
        }

        StarbucksRouteLike like = StarbucksRouteLike.builder()
                .member(currentMember)
                .starbucksRoute(route)
                .build();
        starbucksRouteLikeRepository.save(like);
    }

    @Transactional
    public void unlikeStarbucksRoute(String email, Long routeId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        StarbucksRoute route = findStarbucksRouteById(routeId);
        StarbucksRouteLike routeLike = starbucksRouteLikeRepository.findByMemberAndStarbucksRoute(currentMember, route)
                .orElseThrow(() -> new StarbucksNotFoundException("해당 최적화동선의 좋아요 정보를 찾을 수 업습니다."));

        starbucksRouteLikeRepository.delete(routeLike);
    }

    private StarbucksRoute findStarbucksRouteById(Long routeId) {
        return starbucksRouteRepository.findById(routeId)
                .orElseThrow(() -> new StarbucksRouteNotFoundException("해당 경로가 존재하지 않습니다. routeId=" + routeId));
    }

    private List<StarbucksInfoResDto> convertToDtos(List<StarbucksRouteItem> items) {
        return items.stream()
                .map(item -> {
                    Starbucks s = item.getStarbucks();
                    return new StarbucksInfoResDto(s.getId(), s.getName(), s.getLatitude(), s.getLongitude());
                })
                .toList();
    }
}