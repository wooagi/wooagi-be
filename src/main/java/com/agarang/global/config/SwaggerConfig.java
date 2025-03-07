package com.agarang.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.agarang.global.config<br>
 * fileName       : SwaggerConfig.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-13<br>
 * description    : swagger 관련 설정 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-13          nature1216          최초생성<br>
 * <br>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AGARANG API",
                description = "아가랑 API 서버입니다."
        ),
        servers = {
                @Server(url = "https://agarang.xyz", description = "agarang"),
        }
)
public class SwaggerConfig {

    /**
     * OpenAPI 문서를 구성하는 Bean을 생성합니다.
     *
     * <p>이 메서드는 API 문서에서 JWT 인증을 요구하는 보안 설정을 추가합니다.</p>
     *
     * @return {@link OpenAPI} 객체
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", apiKey())
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"));
    }

    /**
     * JWT 인증을 위한 SecurityScheme을 생성합니다.
     *
     * <p>API 요청 시 `Authorization` 헤더를 사용하여 JWT 토큰을 포함하도록 설정합니다.</p>
     *
     * @return {@link SecurityScheme} 객체
     */
    private SecurityScheme apiKey() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }
}
