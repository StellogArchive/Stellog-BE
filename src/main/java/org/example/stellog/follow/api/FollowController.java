package org.example.stellog.follow.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.follow.api.dto.response.FollowListResDto;
import org.example.stellog.follow.application.FollowService;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
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

    @GetMapping("/following")
    public RspTemplate<FollowListResDto> getFollowings(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "팔로잉 목록 조회에 성공했습니다.",
                followService.getFollowings(email));
    }

    @GetMapping("/followers")
    public RspTemplate<FollowListResDto> getFollowers(@AuthenticatedEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "팔로워 목록 조회에 성공했습니다.",
                followService.getFollowers(email));
    }

    @DeleteMapping("/{followId}")
    public RspTemplate<Void> deleteFollow(@AuthenticatedEmail String email,
                                          @PathVariable(value = "followId") Long followId) {
        followService.deleteFollow(email, followId);
        return new RspTemplate<>(HttpStatus.OK,
                "팔로우가 성공적으로 삭제되었습니다.");
    }
}