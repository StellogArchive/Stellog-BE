package org.example.stellog.room.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.room.api.dto.request.RoomReqDto;
import org.example.stellog.room.api.dto.response.RoomDetailResDto;
import org.example.stellog.room.api.dto.response.RoomListResDto;
import org.example.stellog.room.application.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController implements RoomControllerDocs {
    private final RoomService roomService;

    @PostMapping
    public RspTemplate<Void> createRoom(@AuthenticatedEmail String email, @RequestBody RoomReqDto roomReqDto) {
        roomService.createRoom(email, roomReqDto);
        return new RspTemplate<>(
                HttpStatus.CREATED,
                "방이 성공적으로 생성되었습니다.");
    }

    @GetMapping
    public RspTemplate<RoomListResDto> getAllRoomByEmail(@AuthenticatedEmail String email) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "로그인 한 사용자가 생성한 방 목록을 성공적으로 조회하였습니다.",
                roomService.getAllRoomByEmail(email));
    }

    @GetMapping("/detail/{roomId}")
    public RspTemplate<RoomDetailResDto> getRoomDetails(@AuthenticatedEmail String email, @PathVariable Long roomId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "로그인 한 사용자의 방 상세 정보를 성공적으로 조회하였습니다.",
                roomService.getRoomDetails(email, roomId));
    }

    @PutMapping("/{roomId}")
    public RspTemplate<Void> updateRoom(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody RoomReqDto roomReqDto) {
        roomService.updateRoom(email, roomId, roomReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "방을 성공적으로 수정하였습니다.");
    }

    @DeleteMapping("/{roomId}")
    public RspTemplate<Void> deleteRoom(@AuthenticatedEmail String email, @PathVariable Long roomId) {
        roomService.deleteRoom(email, roomId);
        return new RspTemplate<>(
                HttpStatus.OK,
                "방을 성공적으로 삭제하였습니다.");
    }
}
