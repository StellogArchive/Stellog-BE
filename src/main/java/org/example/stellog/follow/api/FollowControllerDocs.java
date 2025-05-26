package org.example.stellog.follow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stellog.follow.api.dto.response.FollowListResDto;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.member.api.dto.response.MemberInfoResDto;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Follow", description = "팔로우 관련 API")
public interface FollowControllerDocs {
    @Operation(
            summary = "팔로우 등록",
            description = "팔로우를 등록합니다. 이미 팔로우 중인 경우 예외가 발생합니다."
    )
    RspTemplate<Void> saveFollow(@AuthenticatedEmail String email, @PathVariable Long memberId);

    @Operation(
            summary = "팔로우 목록 조회",
            description = "내가 팔로우한 사용자의 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FollowListResDto.class)
            )
    )
    RspTemplate<FollowListResDto> getFollows(@AuthenticatedEmail String email);

    @Operation(
            summary = "팔로잉 목록 조회",
            description = "나를 팔로우한 사용자의 목록을 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FollowListResDto.class)
            )
    )
    RspTemplate<FollowListResDto> getFollowings(@AuthenticatedEmail String email);

    @Operation(
            summary = "팔로워 상세정보 조회",
            description = "팔로워의 상세정보를 조회합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MemberInfoResDto.class)
            )
    )
    RspTemplate<MemberInfoResDto> getFollowerDetail(@AuthenticatedEmail String email,
                                                    @PathVariable Long followerId);

    @Operation(
            summary = "팔로우 취소",
            description = "팔로우를 취소합니다."
    )
    RspTemplate<Void> deleteFollow(@AuthenticatedEmail String email,
                                   @PathVariable Long followId);
}