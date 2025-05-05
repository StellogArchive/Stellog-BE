package org.example.stellog.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.api.jwt.dto.TokenDto;
import org.example.stellog.auth.application.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "OAuth 로그인 관련 API")
@RequestMapping("/api/v1/oauth2")
public class OAuthController {
    private final OAuthService oAuthService;
    private final Map<String, String> redirectUris = Map.of(
            "google", "/api/v1/oauth2/authorization/google",
            "kakao", "/api/v1/oauth2/authorization/kakao"
    );

    @Operation(
            summary = "소셜 로그인 제공자 선택",
            description = "사용자가 로그인하려는 소셜 로그인 제공자(구글 or 카카오)를 선택하고 해당 제공자에게 리다이렉트합니다."
    )
    @GetMapping("/login")
    public void redirectToProvider(@RequestParam("provider") String provider, HttpServletResponse response) throws IOException {
        String redirectUri = redirectUris.get(provider.toLowerCase());
        if (redirectUri == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원하지 않는 소셜 로그인입니다.");
            return;
        }
        response.sendRedirect(redirectUri);
    }

    @Operation(
            summary = "소셜 로그인 인증 URL 생성",
            description = "소셜 로그인 제공자에 따라 인증 URL을 생성하고, 사용자를 해당 인증 페이지로 리다이렉트합니다."
    )
    @GetMapping("/authorization/{provider}")
    public void redirectToAuthorize(
            @PathVariable String provider,
            HttpServletResponse response
    ) throws IOException {
        String redirectUrl = oAuthService.buildAuthUrl(provider);
        response.sendRedirect(redirectUrl);
    }

    @Operation(
            summary = "OAuth 콜백 처리 및 JWT 토큰 반환",
            description = "소셜 로그인 제공자에서 받은 인증 코드로 ID 토큰을 요청하고, 사용자 정보를 처리하여 JWT 토큰을 반환합니다."
    )
    @GetMapping("/callback/{provider}")
    public ResponseEntity<TokenDto> handleCallback(
            @PathVariable String provider,
            @RequestParam String code
    ) {
        TokenDto tokenDto = oAuthService.handleOAuthLogin(provider, code);
        return ResponseEntity.ok().body(tokenDto);
    }
}