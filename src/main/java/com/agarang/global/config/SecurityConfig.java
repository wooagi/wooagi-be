package com.agarang.global.config;

import com.agarang.global.filter.JwtAuthenticationFilter;
import com.agarang.global.util.JwtUtility;
import com.agarang.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * packageName    : com.agarang.global.config<br>
 * fileName       : SecurityConfig.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-01-24<br>
 * description    : Spring Security 설정을 담당하는 구성 클래스입니다.
 * <p>이 클래스는 JWT 기반 인증을 사용하여 보안을 구성하며, CORS 정책을 설정하고,
 * 특정 URL 경로에 대한 접근을 허용하는 등의 역할을 합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux           최초생성<br>
 */
@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 인증 없이 접근이 허용되는 URL 패턴 목록입니다.
     */
    public static final String[] allowUrls = {
            "/", "/api/auth/**", "/swagger-ui/**", "/swagger-ui.html",
            "/v3/api-docs/**", "/swagger-resources/**", "/error",
            "/agarang", "/ws/**", "/app/**"
    };

    private final JwtUtility jwtUtility;
    private final UserService userService;

    /**
     * JWT 인증 필터를 빈으로 등록합니다.
     *
     * <p>이 필터는 요청이 들어올 때 JWT 토큰을 검사하여 인증을 수행하는 역할을 합니다.</p>
     *
     * @return {@link JwtAuthenticationFilter} 객체
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtility, userService);
    }

    /**
     * Spring Security의 보안 필터 체인을 설정합니다.
     *
     * <p>다음과 같은 보안 정책을 설정합니다:</p>
     * <ul>
     *     <li>CORS 설정을 적용합니다.</li>
     *     <li>CSRF 보호를 비활성화합니다.</li>
     *     <li>폼 로그인 및 기본 HTTP 인증을 비활성화합니다.</li>
     *     <li>세션을 사용하지 않고 상태 저장 없이 JWT 기반 인증을 수행합니다.</li>
     *     <li>허용된 URL 패턴에 대해 인증 없이 접근을 허용합니다.</li>
     *     <li>그 외 모든 요청에 대해 인증을 요구합니다.</li>
     *     <li>JWT 인증 필터를 Spring Security 필터 체인에 추가합니다.</li>
     * </ul>
     *
     * @param http                    Spring Security의 {@link HttpSecurity} 객체
     * @param corsConfigurationSource CORS 설정을 제공하는 {@link CorsConfigurationSource} 객체
     * @return {@link SecurityFilterChain} 객체
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource)
                )
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrls).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 정책을 설정하는 빈을 등록합니다.
     *
     * <p>이 메서드는 모든 도메인에서의 요청을 허용하며, 특정 헤더 및 메서드를 허용하도록 설정합니다.</p>
     *
     * <h3>CORS 설정:</h3>
     * <ul>
     *     <li>모든 도메인에서 요청을 허용합니다.</li>
     *     <li>모든 HTTP 메서드를 허용합니다.</li>
     *     <li>인증 정보를 포함하지 않습니다.</li>
     *     <li>특정 헤더(Authorization, Content-Type 등)를 허용합니다.</li>
     *     <li>Set-Cookie 및 Authorization 헤더를 노출합니다.</li>
     *     <li>캐시 최대 유효 기간을 3600초(1시간)로 설정합니다.</li>
     * </ul>
     *
     * @return {@link CorsConfigurationSource} 객체
     */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 도메인에서 요청 허용
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(false);

        // 허용할 요청 헤더
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "baby_id"));
        configuration.setMaxAge(3600L);

        // 클라이언트에서 접근 가능한 헤더
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
