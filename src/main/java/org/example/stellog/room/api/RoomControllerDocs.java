package org.example.stellog.room.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.room.api.dto.request.RoomReqDto;
import org.example.stellog.room.api.dto.response.RoomDetailResDto;
import org.example.stellog.room.api.dto.response.RoomListResDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Room", description = "방 관련 API")
public interface RoomControllerDocs {
    @Operation(
            summary = "방 개설",
            description = "공개 범위를 설정하여 방을 개설합니다."
    )
    RspTemplate<Void> createRoom(@AuthenticatedEmail String email, @RequestBody RoomReqDto roomReqDto);

    @Operation(
            summary = "사용자가 생성한 방 목록 조회",
            description = "현재 로그인 한 사용자의 모든 방 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoomListResDto.class)
            )
    )
    RspTemplate<RoomListResDto> getAllRoomByEmail(@AuthenticatedEmail String email);

    @Operation(
            summary = "방 상세 정보 조회",
            description = "사용자의 방 상세 정보를 조회 합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoomDetailResDto.class)
            )
    )
    RspTemplate<RoomDetailResDto> getRoomDetails(@AuthenticatedEmail String email, @PathVariable Long roomId);

    @Operation(
            summary = "방 수정",
            description = "방을 수정합니다."
    )
    RspTemplate<Void> updateRoom(@AuthenticatedEmail String email, @PathVariable Long roomId, @RequestBody RoomReqDto roomReqDto);

    @Operation(
            summary = "방 삭제",
            description = "방을 삭제합니다."
    )
    RspTemplate<Void> deleteRoom(@AuthenticatedEmail String email, @PathVariable Long roomId);
}
