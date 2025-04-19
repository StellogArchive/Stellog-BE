package org.example.stellog.auth.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.api.jwt.JwtProvider;
import org.example.stellog.auth.api.userInfo.OAuthUserInfo;
import org.example.stellog.auth.client.GoogleOAuthClient;
import org.example.stellog.auth.client.KakaoOAuthClient;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.domain.UserRole;
import org.example.stellog.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final WebClient webClient;
    private final GoogleOAuthClient googleOAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;

    public String buildAuthUrl(String provider) {
        return switch (provider) {
            case "google" -> googleOAuthClient.getAuthUrl();
            case "kakao" -> kakaoOAuthClient.getAuthUrl();
            default -> throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        };
    }

    public String handleOAuthLogin(String provider, String code) {
        String idToken = switch (provider) {
            case "google" -> googleOAuthClient.getIdToken(code);
            case "kakao" -> kakaoOAuthClient.getIdToken(code);
            default -> throw new IllegalArgumentException("지원하지 않는 provider: " + provider);
        };

        Map<String, Object> claims = jwtProvider.parserIdToken(idToken);
        OAuthUserInfo userInfo = OAuthUserInfo.OAuthUserInfoFactory.getUserInfo(provider, claims);

        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(userInfo.getEmail())
                                .name(userInfo.getName())
                                .provider(provider)
                                .userRole(UserRole.ROLE_USER)
                                .build()
                ));
        return jwtProvider.createToken(member.getEmail(), member.getId());
    }
}
