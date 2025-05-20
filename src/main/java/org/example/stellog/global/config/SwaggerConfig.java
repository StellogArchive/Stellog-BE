package org.example.stellog.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.prod-url}")
    private String prodUrl;

    @Value("${swagger.server.local-url}")
    private String localUrl;

    @Bean
    public OpenAPI openAPI() {
        // Security Scheme 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Security Requirement 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .info(new Info().title("stellog API")
                        .description("")
                        .version("1.0.0"))
                .servers(servers())
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("BearerAuth", securityScheme));
    }


    private Info apiInfo() {
        return new Info()
                .title("stellog API")
                .description("")
                .version("1.0.0");
    }

    private List<Server> servers() {
        return List.of(
                new Server()
                        .url(prodUrl)
                        .description("prod develop server"),
                new Server()
                        .url(localUrl)
                        .description("Local development server")
        );
    }
}
