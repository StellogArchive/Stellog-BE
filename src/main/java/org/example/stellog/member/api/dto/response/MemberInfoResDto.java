package org.example.stellog.member.api.dto.response;

public record MemberInfoResDto(
        Long id,
        String name,
        String nickName,
        String email,
        String profileImgUrl,
        String provider,
        int roomCount,
        int reviewCount,
        int followingCount,
        int followerCount
) {
}
