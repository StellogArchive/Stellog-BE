package org.example.stellog.auth.api.userInfo;

public record UserInfo(
        String name,
        String email,
        String nickname,
        String picture
) {
}
