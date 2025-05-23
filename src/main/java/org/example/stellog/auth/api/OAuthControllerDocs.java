package org.example.stellog.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.example.stellog.global.jwt.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Tag(name = "OAuth", description = "OAuth 로그인 관련 API")
public interface OAuthControllerDocs {
    @Operation(
            summary = "소셜 로그인 제공자 선택",
            description = "사용자가 로그인하려는 소셜 로그인 제공자(구글 or 카카오)를 선택하고 해당 제공자에게 리다이렉트합니다."
    )
    void redirectToProvider(@RequestParam("provider") String provider, HttpServletResponse response) throws IOException;

    @Operation(
            summary = "소셜 로그인 인증 URL 생성",
            description = "소셜 로그인 제공자에 따라 인증 URL을 생성하고, 사용자를 해당 인증 페이지로 리다이렉트합니다."
    )
    public void redirectToAuthorize(
            @PathVariable String provider,
            HttpServletResponse response
    ) throws IOException;

    @Operation(
            summary = "OAuth 콜백 처리 및 JWT 토큰 반환",
            description = "소셜 로그인 제공자에서 받은 인증 코드로 ID 토큰을 요청하고, 사용자 정보를 처리하여 JWT 토큰을 반환합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenDto.class)
            )
    )
    public ResponseEntity<TokenDto> handleCallback(
            @PathVariable String provider,
            @RequestParam String code
    );
}