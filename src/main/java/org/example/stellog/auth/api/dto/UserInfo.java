package org.example.stellog.auth.api.dto;

public record UserInfo(
        String name,
        String email,
        String nickname,
        String picture
) {
}
