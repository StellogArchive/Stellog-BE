package org.example.stellog.global.config;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.annotation.resolver.AuthenticatedEmailResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticatedEmailResolver authenticatedEmailResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedEmailResolver);
    }
}
