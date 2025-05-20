package org.example.stellog.member.api.dto.response;

public record MemberInfoDto(
        Long id,
        String name,
        String nickName,
        String email
) {
}
