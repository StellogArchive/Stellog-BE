package org.example.stellog.auth.api.userInfo;

public interface OAuthUserInfo {
    String getEmail();

    String getName();

    String getProvider();

    class OAuthUserInfoFactory {
        public static OAuthUserInfo getUserInfo(String provider, UserInfo attributes) {
            return switch (provider) {
                case "google" -> new GoogleUserInfo(attributes);
                case "kakao" -> new KakaoUserInfo(attributes);
                default -> throw new IllegalArgumentException("Unknown provider: " + provider);
            };
        }
    }
}

