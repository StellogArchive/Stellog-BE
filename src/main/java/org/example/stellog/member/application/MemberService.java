package org.example.stellog.member.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.member.api.dto.request.MemberUpdateRequestDto;
import org.example.stellog.member.api.dto.response.MemberInfoDto;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.exception.MemberNotFoundException;
import org.example.stellog.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void updateMember(String email, MemberUpdateRequestDto memberUpdateRequestDto) {
        // TODO: MemberRoomService
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        member.update(memberUpdateRequestDto.nickName());
    }

    public MemberInfoDto getMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        return new MemberInfoDto(member.getId(),
                member.getName(),
                member.getNickName(),
                member.getEmail()
        );
    }
}
