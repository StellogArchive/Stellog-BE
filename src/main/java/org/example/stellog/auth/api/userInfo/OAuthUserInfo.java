package org.example.stellog.auth.api.userInfo;

import java.util.Map;

public interface OAuthUserInfo {
    String getEmail();

    String getName();

    String getProvider();

    class OAuthUserInfoFactory {
        public static OAuthUserInfo getUserInfo(String provider, Map<String, Object> attributes) {
            return switch (provider) {
                case "google" -> new GoogleUserInfo(attributes);
                case "kakao" -> new KakaoUserInfo(attributes);
                default -> throw new IllegalArgumentException("Unknown provider: " + provider);
            };
        }
    }
}

