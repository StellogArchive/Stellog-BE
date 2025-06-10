package org.example.stellog.member.api;

import lombok.RequiredArgsConstructor;
import org.example.stellog.gcs.application.GCSService;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.template.RspTemplate;
import org.example.stellog.member.api.dto.request.MemberUpdateReqDto;
import org.example.stellog.member.api.dto.response.MemberInfoResDto;
import org.example.stellog.member.api.dto.response.MemberListResDto;
import org.example.stellog.member.application.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController implements MemberControllerDocs {
    private final MemberService memberService;
    private final GCSService gcsService;

    @PutMapping("/nickname")
    public RspTemplate<String> updateMemberNickName(@AuthenticatedEmail String email, @RequestBody MemberUpdateReqDto memberUpdateReqDto) {
        memberService.updateMemberNickNme(email, memberUpdateReqDto);
        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 닉네임이 성공적으로 수정되었습니다."
        );
    }

    @PutMapping("/profile")
    public RspTemplate<String> updateProfileImg(@AuthenticatedEmail String email,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        String profileImgUrl = gcsService.uploadFile(email, file);
        memberService.updateMemberProfileImg(email, profileImgUrl);

        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 프로필 이미지가 성공적으로 수정되었습니다."
        );
    }

    @GetMapping("/info")
    public RspTemplate<MemberInfoResDto> getMember(@AuthenticatedEmail String email) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 정보가 성공적으로 조회되었습니다.",
                memberService.getMember(email)
        );
    }

    @GetMapping("/info/{memberId}")
    public RspTemplate<MemberInfoResDto> getMemberById(@AuthenticatedEmail String email,
                                                       @PathVariable(value = "memberId") Long memberId) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 정보가 성공적으로 조회되었습니다.",
                memberService.getMemberById(email, memberId)
        );
    }

    @GetMapping()
    public RspTemplate<MemberListResDto> getAllMembers(@AuthenticatedEmail String email,
                                                       @RequestParam("name") String name) {
        return new RspTemplate<>(
                HttpStatus.OK,
                "회원 목록이 성공적으로 조회되었습니다.",
                memberService.getAllMembers(email, name)
        );
    }
}
