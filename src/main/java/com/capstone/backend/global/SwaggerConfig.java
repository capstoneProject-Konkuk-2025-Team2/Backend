package com.capstone.backend.global;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("https://capstone-backend.o-r.kr").description("Production Server"),
                        new Server().url("http://localhost:8080").description("Local Server")
                ))
                .components(components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement());
    }

    private Info apiInfo() {
        return new Info()
                .title("Capstone Design API Docs")
                .description("졸업프로젝트 관련 spring 서버 Api Document 입니다.");
    }

    // JWT 토큰을 위한 보안 컴포넌트 설정
    private Components components() {
        String securityScheme = "JWT TOKEN";
        return new Components()
                .addSecuritySchemes(securityScheme, new SecurityScheme()
                        .name(securityScheme)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));
    }

    // 보안 요구사항 설정
    private SecurityRequirement securityRequirement() {
        String securityScheme = "JWT TOKEN";
        return new SecurityRequirement().addList(securityScheme);
    }
}
