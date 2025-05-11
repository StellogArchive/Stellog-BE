package org.example.stellog.room.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.room.api.dto.request.RoomRequestDto;
import org.example.stellog.room.api.dto.response.RoomDetailResponseDto;
import org.example.stellog.room.api.dto.response.RoomResponseDto;
import org.example.stellog.room.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Room", description = "방 관련 API")
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;

    @Operation(
            summary = "방 개설",
            description = "공개 범위를 설정하여 방을 개설합니다."
    )
    @PostMapping
    public RspTemplate<Void> createRoom(@AuthenticatedEmail String email, @RequestBody RoomRequestDto roomRequestDto) {
        roomService.createRoom(email, roomRequestDto);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "방이 성공적으로 생성되었습니다.");
    }

    @Operation(
            summary = "사용자가 생성한 방 목록 조회",
            description = "현재 로그인 한 사용자의 모든 방 목록을 조회합니다."
    )
    @GetMapping
    public RspTemplate<List<RoomResponseDto>> getAllRoomByEmail(@AuthenticatedEmail String email) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "로그인 한 사용자가 생성한 방 목록을 성공적으로 조회하였습니다.",
                roomService.getAllRoom(email));
    }

    @Operation(
            summary = "방 상세 정보 조회",
            description = "사용자의 방 상세 정보를 조회 합니다."
    )
    @GetMapping("/detail")
    public RspTemplate<RoomDetailResponseDto> getRoomDetails(@AuthenticatedEmail String email, @RequestParam Long roomId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "로그인 한 사용자의 방 상세 정보를 성공적으로 조회하였습니다.",
                roomService.getRoomDetails(email, roomId));
    }

    @Operation(
            summary = "방 수정",
            description = "방을 수정합니다."
    )
    @PutMapping
    public RspTemplate<Void> updateRoom(@AuthenticatedEmail String email, @RequestParam Long roomId, @RequestBody RoomRequestDto roomRequestDto) {
        roomService.updateRoom(email, roomId, roomRequestDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "방을 성공적으로 수정하였습니다.");
    }

    @Operation(
            summary = "방 삭제",
            description = "방을 삭제합니다."
    )
    @DeleteMapping
    public RspTemplate<Void> deleteRoom(@AuthenticatedEmail String email, @RequestParam Long roomId) {
        roomService.deleteRoom(email, roomId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "방을 성공적으로 삭제하였습니다.");
    }
}
