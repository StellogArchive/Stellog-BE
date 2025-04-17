package org.example.stellog.auth.application;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.api.jwt.JwtProvider;
import org.example.stellog.auth.api.userInfo.OAuthUserInfo;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.domain.UserRole;
import org.example.stellog.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final WebClient webClient;

    @Value("${oauth.google.client-id}")
    private String googleClientId;
    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;
    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;
    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public String buildAuthUrl(String provider) {
        if (provider.equals("google")) {
            return "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + googleClientId +
                "&redirect_uri=" + googleRedirectUri +
                "&response_type=code" +
                "&scope=email%20profile";
        } else if (provider.equals("kakao")) {
            return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri +
                "&response_type=code";
        }
        throw new IllegalArgumentException("지원하지 않는 provider" + provider);
    }

    public String handleOAuthLogin(String provider, String code) {
        String accessToken = getAccessToken(provider, code);
        OAuthUserInfo userInfo = fetchUserInfo(provider, accessToken);

        Member member = memberRepository.findByEmail(userInfo.getEmail())
            .orElseGet(() -> memberRepository.save(
                Member.builder()
                    .email(userInfo.getEmail())
                    .name(userInfo.getName())
                    .provider(provider)
                    .providerId(userInfo.getProviderId())
                    .userRole(UserRole.ROLE_USER)
                    .build()
            ));
        return jwtProvider.createToken(member.getEmail(), member.getId());
    }

    private String getAccessToken(String provider, String code) {
        MultiValueMap<String, String> params = getParams(provider, code);

        String tokenUri = provider.equals("google") ?
            "https://oauth2.googleapis.com/token" :
            "https://kauth.kakao.com/oauth/token";

        Map<String, Object> response = webClient.post()
            .uri(tokenUri)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .bodyValue(params)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

        if (response == null || !response.containsKey("access_token")) {
            throw new IllegalStateException("엑세스 토큰을 가져오지 못했습니다: " + response);
        }

        return response.get("access_token").toString();
    }

    private MultiValueMap<String, String> getParams(String provider, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        if (provider.equals("google")) {
            params.add("code", code);
            params.add("client_id", googleClientId);
            params.add("client_secret", googleClientSecret);
            params.add("redirect_uri", googleRedirectUri);
            params.add("grant_type", "authorization_code");
        } else if (provider.equals("kakao")) {
            params.add("code", code);
            params.add("client_id", kakaoClientId);
            params.add("redirect_uri", kakaoRedirectUri);
            params.add("grant_type", "authorization_code");
        } else {
            throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        }
        return params;
    }

    private OAuthUserInfo fetchUserInfo(String provider, String accessToken) {

        String userInfoUri = provider.equals("google") ?
            "https://www.googleapis.com/oauth2/v2/userinfo" :
            "https://kapi.kakao.com/v2/user/me";

        Map<String, Object> attributes = webClient.get()
            .uri(userInfoUri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

        return OAuthUserInfo.OAuthUserInfoFactory.getUserInfo(provider, attributes);
    }
}
