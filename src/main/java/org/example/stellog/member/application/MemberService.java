package org.example.stellog.member.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.api.dto.request.MemberUpdateRequestDto;
import org.example.stellog.member.api.dto.response.MemberInfoDto;
import org.example.stellog.member.api.dto.response.MemberListInfoDto;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
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

    public MemberListInfoDto getMemberList(String email, String name) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        List<Member> members = memberRepository.findByNameContaining(name);

        List<MemberInfoDto> memberList = members.stream()
                .filter(m -> !m.getId().equals(currentMember.getId()))
                .map(m -> new MemberInfoDto(
                        m.getId(),
                        m.getName(),
                        m.getNickName(),
                        m.getEmail(),
                        m.getProfileImgUrl()))
                .toList();

        return new MemberListInfoDto(memberList);
    }
}
