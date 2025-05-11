package org.example.stellog.room.api.dto.response;

import org.example.stellog.member.domain.Member;

public record MemberSimpleDto(
        Long id,
        String name
) {
    public static MemberSimpleDto from(Member member) {
        return new MemberSimpleDto(member.getId(), member.getName());
    }
}
