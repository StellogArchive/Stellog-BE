package org.example.stellog.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.member.api.dto.request.MemberUpdateReqDto;
import org.example.stellog.member.api.dto.response.MemberInfoResDto;
import org.example.stellog.member.api.dto.response.MemberListResDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberControllerDocs {
    @Operation(
            summary = "회원 닉네임 수정",
            description = "회원 닉네임을 수정합니다"
    )
    RspTemplate<String> updateMemberNickName(@AuthenticatedEmail String email, @RequestBody MemberUpdateReqDto memberUpdateReqDto);

    @Operation(
            summary = "회원 프로필 이미지 수정",
            description = "회원 프로필 이미지를 수정합니다."
    )
    RspTemplate<String> updateProfileImg(@AuthenticatedEmail String email,
                                         @RequestParam("file") MultipartFile file) throws IOException;

    @Operation(
            summary = "회원 정보 조회",
            description = "마이페이지에서 회원 정보를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberInfoResDto.class)
            )
    )
    RspTemplate<MemberInfoResDto> getMember(@AuthenticatedEmail String email);

    @Operation(
            summary = "회원 목록 조회",
            description = "회원 목록을 조회합니다. 방 생성 시 참여할 회원을 선택하기 위해 사용됩니다. 이름 중 하나 글자만 입력해도 조회가 됩니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberListResDto.class)
            )
    )
    RspTemplate<MemberListResDto> getAllMembers(@AuthenticatedEmail String email,
                                                @RequestParam("name") String name);
}
