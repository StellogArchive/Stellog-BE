package org.example.stellog.auth.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.application.OAuthService;
import org.example.stellog.global.jwt.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuthController implements OAuthControllerDocs {
    private final OAuthService oAuthService;
    private final Map<String, String> redirectUris = Map.of(
            "google", "/api/v1/oauth2/authorization/google",
            "kakao", "/api/v1/oauth2/authorization/kakao"
    );

    @GetMapping("/login")
    public void redirectToProvider(@RequestParam("provider") String provider, HttpServletResponse response) throws IOException {
        String redirectUri = redirectUris.get(provider.toLowerCase());
        if (redirectUri == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원하지 않는 소셜 로그인입니다.");
            return;
        }
        response.sendRedirect(redirectUri);
    }

    @GetMapping("/authorization/{provider}")
    public void redirectToAuthorize(
            @PathVariable(value = "provider") String provider,
            HttpServletResponse response
    ) throws IOException {
        String redirectUrl = oAuthService.buildAuthUrl(provider);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback/{provider}")
    public ResponseEntity<TokenDto> handleCallback(
            @PathVariable(value = "provider") String provider,
            @RequestParam("code") String code
    ) {
        TokenDto tokenDto = oAuthService.handleOAuthLogin(provider, code);
        return ResponseEntity.ok().body(tokenDto);
    }
}