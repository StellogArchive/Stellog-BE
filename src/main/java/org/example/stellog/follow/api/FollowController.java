package org.example.stellog.follow.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.follow.api.dto.response.FollowListResDto;
import org.example.stellog.follow.application.FollowService;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.member.api.dto.response.MemberInfoResDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
public class FollowController implements FollowControllerDocs {
    private final FollowService followService;

    @PostMapping("/{followId}")
    public RspTemplate<Void> saveFollow(@AuthenticatedEmail String email, @PathVariable(value = "followId") Long memberId) {
        followService.saveFollow(email, memberId);
        return new RspTemplate<>(HttpStatus.CREATED,
                "팔로우가 성공적으로 등록되었습니다.");
    }

    @GetMapping("/follow")
    public RspTemplate<FollowListResDto> getFollows(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "팔로우 목록 조회에 성공했습니다.",
                followService.getFollows(email));
    }

    @GetMapping("/following")
    public RspTemplate<FollowListResDto> getFollowings(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "팔로잉 목록 조회에 성공했습니다.",
                followService.getFollowings(email));
    }

    @GetMapping("/detail/{followerId}")
    public RspTemplate<MemberInfoResDto> getFollowerDetail(@AuthenticatedEmail String email,
                                                           @PathVariable(value = "followerId") Long followerId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "팔로워 목록이 성공적으로 조회되었습니다.",
                followService.getFollowerDetail(email, followerId)
        );
    }

    @DeleteMapping("/{followId}")
    public RspTemplate<Void> deleteFollow(@AuthenticatedEmail String email,
                                          @PathVariable(value = "followId") Long followId) {
        followService.deleteFollow(email, followId);
        return new RspTemplate<>(HttpStatus.OK,
                "팔로우가 성공적으로 삭제되었습니다.");
    }
}