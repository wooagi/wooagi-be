package com.agarang.global.filter;

import com.agarang.global.exception.ErrorCode;
import com.agarang.global.util.JwtUtility;
import com.agarang.domain.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * packageName    : com.agarang.global.filter<br>
 * fileName       : JwtAuthenticationFilter.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-24<br>
 * description    : JWT 인증을 처리하는 필터 클래스입니다.
 * <p>이 필터는 요청이 들어올 때마다 실행되며, JWT 토큰을 검증하여 사용자 인증을 수행합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-24          nature1216          최초생성<br>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtility jwtUtility;
    private final UserService userService;

    private static final String[] AllowUrls = new String[]{
            "/api/auth/",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/error",
            "/agarang",
            "/ws/**",
            "/app/**"
    };

    /**
     * 요청을 가로채 JWT 검증을 수행하는 메서드입니다.
     *
     * <p>허용된 경로(AllowUrls)에 대해서는 필터를 통과시키며,
     * Authorization 헤더가 있는 경우 JWT를 검증하여 유효한 경우 보안 컨텍스트를 설정합니다.</p>
     *
     * @param request     HTTP 요청
     * @param response    HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 필터 처리 중 예외 발생 시
     * @throws IOException      입출력 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        // JWT 검증 제외
        if (Arrays.stream(AllowUrls).anyMatch(uri::startsWith) && !request.getRequestURI().equals("/api/auth/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            writeErrorResponse(response, ErrorCode.UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        if (jwtUtility.validateToken(token)) {
            processValidAccessToken(token);
        } else {
            writeErrorResponse(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 유효한 JWT 토큰을 처리하여 보안 컨텍스트를 설정하는 메서드입니다.
     *
     * <p>JWT에서 사용자 정보를 추출하여 {@link UserDetails} 객체를 생성하고,
     * Spring Security의 컨텍스트에 인증 객체를 설정합니다.</p>
     *
     * @param accessToken 검증된 액세스 토큰
     */
    private void processValidAccessToken(String accessToken) {
        UserDetails userDetails = userService.loadUserByUsername(jwtUtility.getUserId(accessToken));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 오류 응답을 생성하여 클라이언트에 반환하는 메서드입니다.
     *
     * <p>JWT가 유효하지 않은 경우 해당 오류 코드를 JSON 형식으로 응답합니다.</p>
     *
     * @param response  HTTP 응답 객체
     * @param errorCode 발생한 오류 코드
     * @throws IOException 응답 작성 중 예외 발생 시
     */
    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(
                String.format("{\"status\": \"%s\", \"name\": \"%s\", \"code\": \"%s\", \"message\": \"%s\"}",
                        errorCode.getHttpStatus().value(),
                        errorCode.name(),
                        errorCode.getCode(),
                        errorCode.getMessage())
        );
    }
}
