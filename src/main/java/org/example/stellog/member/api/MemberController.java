package org.example.stellog.member.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.member.api.dto.request.MemberUpdateRequestDto;
import org.example.stellog.member.api.dto.response.MemberInfoDto;
import org.example.stellog.member.application.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {
    private final MemberService memberService;

    @PutMapping
    public RspTemplate<String> updateMember(@AuthenticatedEmail String email, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.updateMember(email, memberUpdateRequestDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 정보가 성공적으로 수정되었습니다."
        );
    }

    @GetMapping
    public RspTemplate<MemberInfoDto> getMember(@AuthenticatedEmail String email) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 정보가 성공적으로 조회되었습니다.",
                memberService.getMember(email)
        );
    }
}
