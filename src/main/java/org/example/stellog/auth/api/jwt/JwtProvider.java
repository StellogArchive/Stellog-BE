package org.example.stellog.auth.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(String email, Long userId) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 3600 * 1000); // 1시간

        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key, SIG.HS256)
            .compact();
    }

    public String getEmailFromToken(String token) {
        JwtParser parser = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
            .build();

        return parser.parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public Long getUserIdFromToken(String token) {
        JwtParser parser = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.get("userId", Long.class);
    }
}