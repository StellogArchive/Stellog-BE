package org.example.stellog.global.annotation.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stellog.global.annotation.AuthenticatedEmail;
import org.example.stellog.global.annotation.exception.UnauthorizedException;
import org.example.stellog.global.jwt.JwtProvider;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticatedEmailResolver implements HandlerMethodArgumentResolver {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedEmail.class)
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && !authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
        }
        String token = authHeader.substring(BEARER_PREFIX.length());
        String email = jwtProvider.extractEmailFromToken(token);

        if (email == null || email.isEmpty()) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다: 이메일 정보를 찾을 수 없습니다.");
        }
        return email;
    }
}
