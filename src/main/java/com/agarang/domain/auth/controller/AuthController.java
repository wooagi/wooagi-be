package com.agarang.domain.auth.controller;

import com.agarang.domain.auth.dto.request.KakaoLoginRequest;
import com.agarang.domain.auth.dto.TokenPair;
import com.agarang.domain.auth.dto.request.RefreshTokenRequest;
import com.agarang.domain.auth.dto.response.KakaoLoginResponse;
import com.agarang.domain.auth.service.LoginService;
import com.agarang.domain.auth.service.TokenService;
import com.agarang.domain.user.dto.CustomUserDetails;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.util.JwtUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * packageName    : com.agarang.domain.auth.controller<br>
 * fileName       : AuthController.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 22.<br>
 * description    : Auth 에 관한 controller 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          okeio           최초생성<br>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "인증 API", description = "사용자 인증 및 토큰 관련 기능을 제공하는 API")
public class AuthController {
    private final TokenService tokenService;
    private final LoginService loginService;
    private final JwtUtility jwtUtility;

    /**
     * 카카오 로그인 및 회원가입을 처리합니다.
     *
     * <p>
     * 카카오 로그인 요청을 받아 사용자가 존재하면 로그인하고, 존재하지 않으면 회원가입 후 로그인합니다.
     * </p>
     *
     * @param kakaoLoginRequest 카카오 로그인 요청 데이터
     * @return 로그인 결과를 포함한 {@link KakaoLoginResponse} 객체
     */
    @Operation(summary = "카카오 로그인", description = "카카오 OAuth를 사용한 회원가입 및 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KakaoLoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "카카오 로그인 요청 객체")
            KakaoLoginRequest kakaoLoginRequest) {
        log.info("회원가입 및 로그인 요청");

        KakaoLoginResponse response = loginService.kakaoLoginOrRegister(kakaoLoginRequest);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "현재 로그인한 사용자의 리프레시 토큰을 삭제하여 로그아웃 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        tokenService.deleteRefreshToken(userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenPair.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "리프레시 토큰 요청 객체")
            RefreshTokenRequest refreshTokenRequest) {
        log.info("액세스 토큰 재발급 요청");

        String refreshToken = refreshTokenRequest.getRefreshToken();

        // 리프레시 토큰 검증
        if (!jwtUtility.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        int userId = Integer.parseInt(jwtUtility.getUserId(refreshToken));

        // Redis에 저장된 리프레시 토큰과 비교
        String storedRefreshToken = tokenService.getRefreshToken(userId);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtUtility.generateAccessToken(userId);

        log.info("새로운 액세스 토큰 발급: {}", newAccessToken);
        return ResponseEntity.ok(new TokenPair(newAccessToken, refreshToken));
    }

}
