package org.example.stellog.member.api.dto.response;

import java.util.List;

public record MemberListResDto(
        List<MemberInfoResDto> members
) {
}
