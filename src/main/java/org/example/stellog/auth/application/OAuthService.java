package org.example.stellog.auth.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.api.jwt.JwtProvider;
import org.example.stellog.auth.api.userInfo.OAuthUserInfo;
import org.example.stellog.auth.client.GoogleOAuthClient;
import org.example.stellog.auth.client.KakaoOAuthClient;
import org.example.stellog.auth.exception.OAuthLoginFailedException;
import org.example.stellog.auth.exception.UnsupportedProviderException;
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
        String idToken;

        try {
            idToken = switch (provider) {
                case "google" -> googleOAuthClient.getIdToken(code);
                case "kakao" -> kakaoOAuthClient.getIdToken(code);
                default -> throw new UnsupportedProviderException(provider);
            };
        } catch (Exception e) {
            throw new OAuthLoginFailedException(e.getMessage());
        }

        Map<String, Object> claims;
        try {
            claims = jwtProvider.parserIdToken(idToken);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("ID Token 파싱 실패: " + e.getMessage());
        }

        OAuthUserInfo userInfo;
        try {
            userInfo = OAuthUserInfo.OAuthUserInfoFactory.getUserInfo(provider, claims);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("UserInfo 생성 실패: " + e.getMessage());
        }

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
