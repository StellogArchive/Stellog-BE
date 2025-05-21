package org.example.stellog.member.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.api.dto.request.MemberUpdateRequestDto;
import org.example.stellog.member.api.dto.response.MemberInfoDto;
import org.example.stellog.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRoomService memberRoomService;

    @Transactional
    public void updateMemberNickNme(String email, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRoomService.findMemberByEmail(email);
        member.updateNickName(memberUpdateRequestDto.nickName());
    }

    @Transactional
    public void updateMemberProfileImg(String email, String profileImgUrl) {
        Member member = memberRoomService.findMemberByEmail(email);
        member.updateProfileImgUrl(profileImgUrl);
    }


    public MemberInfoDto getMember(String email) {
        Member member = memberRoomService.findMemberByEmail(email);
        return new MemberInfoDto(member.getId(),
                member.getName(),
                member.getNickName(),
                member.getEmail(),
                member.getProfileImgUrl()
        );
    }
}
