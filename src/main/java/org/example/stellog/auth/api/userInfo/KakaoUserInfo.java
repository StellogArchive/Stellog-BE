package org.example.stellog.auth.api.userInfo;

public class KakaoUserInfo implements OAuthUserInfo {
    private final UserInfo attributes;

    public KakaoUserInfo(UserInfo attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        return attributes.email();
    }

    @Override
    public String getName() {
        return attributes.name();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }
}

