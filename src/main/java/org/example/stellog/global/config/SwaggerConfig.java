package org.example.stellog.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(apiInfo())
            .servers(servers());
    }

    private Info apiInfo() {
        return new Info()
            .title("stellog API")
            .description("")
            .version("1.0.0");
    }

    private List<Server> servers() {
        return List.of(new Server()
            .url("http://localhost:8080")
            .description("Local development server")
        );
    }
}
