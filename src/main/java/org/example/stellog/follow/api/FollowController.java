package org.example.stellog.follow.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.stellog.follow.api.dto.response.FollowListResponseDto;
import org.example.stellog.follow.application.FollowService;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.member.api.dto.response.MemberInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Follow", description = "팔로우 관련 API")
@RequestMapping("/api/v1/follows")
public class FollowController {
    private final FollowService followService;

    @Operation(
            summary = "팔로우 등록",
            description = "팔로우를 등록합니다. 이미 팔로우 중인 경우 예외가 발생합니다."
    )
    @PostMapping("{followId}")
    public RspTemplate<Void> saveFollow(@AuthenticatedEmail String email, @PathVariable("followId") Long memberId) {
        followService.saveFollow(email, memberId);
        return new RspTemplate<>(HttpStatus.CREATED,
                "팔로우가 성공적으로 등록되었습니다.");
    }

    @Operation(
            summary = "팔로우 목록 조회",
            description = "내가 팔로우한 사용자의 목록을 조회합니다."
    )
    @GetMapping("/follow")
    public RspTemplate<FollowListResponseDto> getFollows(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "팔로우 목록 조회에 성공했습니다.",
                followService.getFollows(email));
    }

    @Operation(
            summary = "팔로잉 목록 조회",
            description = "나를 팔로우한 사용자의 목록을 조회합니다."
    )
    @GetMapping("/following")
    public RspTemplate<FollowListResponseDto> getFollowings(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "팔로잉 목록 조회에 성공했습니다.",
                followService.getFollowings(email));
    }

    @Operation(
            summary = "팔로워 상세정보 조회",
            description = "팔로워의 상세정보를 조회합니다."
    )
    @GetMapping("/detail/{followerId}")
    public RspTemplate<MemberInfoDto> getFollowers(@AuthenticatedEmail String email,
                                                   @PathVariable Long followerId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "팔로워 목록이 성공적으로 조회되었습니다.",
                followService.getFollowers(email, followerId)
        );
    }

    @Operation(
            summary = "팔로우 취소",
            description = "팔로우를 취소합니다."
    )
    @DeleteMapping("/{followId}")
    public RspTemplate<Void> deleteFollow(@AuthenticatedEmail String email,
                                          @PathVariable("followId") Long followId) {
        followService.deleteFollow(email, followId);
        return new RspTemplate<>(HttpStatus.OK,
                "팔로우가 성공적으로 삭제되었습니다.");
    }
}