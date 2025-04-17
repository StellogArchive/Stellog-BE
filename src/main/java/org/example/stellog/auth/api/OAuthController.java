package org.example.stellog.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.application.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuthController {

    private final OAuthService oAuthService;
    private final Map<String, String> redirectUris = Map.of(
        "google", "/api/v1/oauth2/authorization/google",
        "kakao", "/api/v1/oauth2/authorization/kakao"
    );

    @Operation
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
        @PathVariable String provider,
        HttpServletResponse response
    ) throws IOException {
        String redirectUrl = oAuthService.buildAuthUrl(provider);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback/{provider}")
    public ResponseEntity<?> handleCallback(
        @PathVariable String provider,
        @RequestParam String code
    ) {
        String jwt = oAuthService.handleOAuthLogin(provider, code);
        return ResponseEntity.ok().body(Map.of("token", jwt));
    }
}