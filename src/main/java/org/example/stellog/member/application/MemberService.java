package org.example.stellog.member.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.api.dto.request.MemberUpdateReqDto;
import org.example.stellog.member.api.dto.response.MemberInfoResDto;
import org.example.stellog.member.api.dto.response.MemberListResDto;
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
    public void updateMemberNickNme(String email, MemberUpdateReqDto memberUpdateReqDto) {
        Member member = memberRoomService.findMemberByEmail(email);
        member.updateNickName(memberUpdateReqDto.nickName());
    }

    @Transactional
    public void updateMemberProfileImg(String email, String profileImgUrl) {
        Member member = memberRoomService.findMemberByEmail(email);
        member.updateProfileImgUrl(profileImgUrl);
    }

    public MemberInfoResDto getMember(String email) {
        Member member = memberRoomService.findMemberByEmail(email);
        return new MemberInfoResDto(member.getId(),
                member.getName(),
                member.getNickName(),
                member.getEmail(),
                member.getProfileImgUrl()
        );
    }

    public MemberListResDto getMemberList(String email, String name) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        List<Member> members = memberRepository.findByNameContaining(name);

        List<MemberInfoResDto> memberList = members.stream()
                .filter(m -> !m.getId().equals(currentMember.getId()))
                .map(m -> new MemberInfoResDto(
                        m.getId(),
                        m.getName(),
                        m.getNickName(),
                        m.getEmail(),
                        m.getProfileImgUrl()))
                .toList();

        return new MemberListResDto(memberList);
    }
}
