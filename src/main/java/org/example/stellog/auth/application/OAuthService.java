package org.example.stellog.auth.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.api.jwt.JwtProvider;
import org.example.stellog.auth.api.jwt.dto.TokenDto;
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

    public TokenDto handleOAuthLogin(String provider, String code) {
        String idToken = getIdToken(provider, code);
        Map<String, Object> claims = parserIdTokne(idToken);
        OAuthUserInfo userInfo = getUserInfo(provider, claims);
        Member member = getOrCreateMember(userInfo, provider);
        String token = jwtProvider.createToken(member.getEmail(), member.getId());
        return new TokenDto(token);
    }

    private String getIdToken(String provider, String code) {
        try {
            return switch (provider) {
                case "google" -> googleOAuthClient.getIdToken(code);
                case "kakao" -> kakaoOAuthClient.getIdToken(code);
                default -> throw new UnsupportedProviderException(provider);
            }
                    ;
        } catch (Exception e) {
            throw new OAuthLoginFailedException(e.getMessage());
        }
    }

    private Map<String, Object> parserIdTokne(String idToken) {
        try {
            return jwtProvider.parserIdToken(idToken);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("ID Token 파싱 실패: " + e.getMessage());
        }
    }

    private OAuthUserInfo getUserInfo(String provider, Map<String, Object> claims) {
        try {
            return OAuthUserInfo.OAuthUserInfoFactory.getUserInfo(provider, claims);
        } catch (Exception e) {
            throw new OAuthLoginFailedException("UserInfo 생성 실패: " + e.getMessage());
        }
    }


    private Member getOrCreateMember(OAuthUserInfo userInfo, String provider) {
        return memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(userInfo.getEmail())
                                .name(userInfo.getName())
                                .provider(provider)
                                .userRole(UserRole.ROLE_USER)
                                .build()
                ));
    }
}
