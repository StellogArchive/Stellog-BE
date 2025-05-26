package org.example.stellog.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.stellog.auth.api.userInfo.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry}")
    private long expiry;

    private final ObjectMapper objectMapper;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // secret 값을 Base64 디코딩하여 Key 생성
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String email, Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + this.expiry);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, SIG.HS256)
                .compact();
    }

    public UserInfo parserIdToken(String idToken) {
        String[] tokenParts = idToken.split("\\.");
        if (tokenParts.length != 3) {
            throw new IllegalArgumentException("유효하지 않은 ID 토큰 형식입니다.");
        }

        String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(payload, UserInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("ID 토큰 파싱 중 오류 발생", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser() // 서명, 만료 시간, 포맷 등 검증
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token); // 유효한 토큰이면 여기서 예외 없이 끝남
            return true;
        } catch (Exception e) {
            // 서명 불일치, 만료, 형식 오류 등의 경우 모두 false
            return false;
        }
    }

    public String extractEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Long extractUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}