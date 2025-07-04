package org.example.stellog.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stellog.global.filter.exception.AuthenticationException;
import org.example.stellog.global.jwt.JwtProvider;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckFilter extends GenericFilterBean {

    private static final String[] whiteList = {
            "*", // 일단 다 열어둠
//            "/",
//            "/api/v1/oauth2/login/**",
//            "/api/v1/oauth2/callback/**"
    };

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        try {
            log.info("인증 체크 필터 시작{}", requestURI);
            if (!isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행{}", requestURI);
                String token = resolveToken(httpRequest);
                if (token == null || !jwtProvider.validateToken(token)) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                }
                // 토큰이 유효한 경우 사용자 정보를 로그로 출력

                String email = jwtProvider.extractEmailFromToken(token);
                Long userId = jwtProvider.extractUserIdFromToken(token);

                httpRequest.setAttribute("userId", userId);
                httpRequest.setAttribute("email", email);

                log.info("인증된 사용자: userId={}, email={}", userId, email);
            }
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            throw e;
        } finally {
            log.info("인증 체크 필터 종료{}", requestURI);
        }
    }

    private boolean isLoginCheckPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(whiteList, requestURI); // 화이트리스트에 있는 경로는 true 반환
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}