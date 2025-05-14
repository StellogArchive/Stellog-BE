package org.example.stellog.room.api.dto.response;

import org.example.stellog.member.domain.Member;

public record MemberInfoDto(
        Long id,
        String name
) {
    public static MemberInfoDto from(Member member) {
        return new MemberInfoDto(member.getId(), member.getName());
    }
}
