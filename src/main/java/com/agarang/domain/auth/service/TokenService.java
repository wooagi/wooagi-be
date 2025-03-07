package com.agarang.domain.auth.service;

import com.agarang.domain.auth.dto.TokenPair;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.util.JwtUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * packageName    : com.agarang.domain.auth.service<br>
 * fileName       : TokenService.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 22.<br>
 * description    : JWT 토큰 관련 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          okeio           최초 생성<br>
 */
@Tag(name = "토큰 서비스", description = "JWT 토큰 관련 서비스")
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtility jwtUtility;

    /**
     * 새로운 액세스 토큰과 리프레시 토큰을 생성합니다.
     *
     * <p>
     * 이 메서드는 주어진 사용자 ID를 기반으로 JWT 토큰을 생성하고,
     * 생성된 리프레시 토큰을 Redis에 저장합니다.
     * </p>
     *
     * @param userId 사용자 ID
     * @return 생성된 {@link TokenPair} 객체 (액세스 토큰 및 리프레시 토큰 포함)
     */
    @Operation(summary = "JWT 토큰 생성", description = "사용자의 액세스 토큰과 리프레시 토큰을 생성합니다.")
    public TokenPair generateTokens(int userId) {
        String accessToken = jwtUtility.generateAccessToken(userId);
        String refreshToken = jwtUtility.generateRefreshToken(userId);

        saveRefreshToken(userId, refreshToken);
        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * 리프레시 토큰을 Redis에 저장합니다.
     *
     * <p>
     * 사용자의 리프레시 토큰을 Redis에 저장하여, 이후 액세스 토큰 재발급 시 활용됩니다.
     * 토큰은 30일 동안 유지됩니다.
     * </p>
     *
     * @param userId       사용자 ID
     * @param refreshToken 저장할 리프레시 토큰
     * @throws BusinessException 토큰 저장 실패 시 예외 발생
     */
    @Operation(summary = "리프레시 토큰 저장", description = "사용자의 리프레시 토큰을 Redis에 저장합니다.")
    public void saveRefreshToken(int userId, String refreshToken) {
        try {
            redisTemplate.opsForValue().set(String.valueOf(userId), refreshToken, 30, TimeUnit.DAYS);
            log.info("Saved refresh token for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to save refresh token for user: {}", userId, e);
            throw new BusinessException(ErrorCode.TOKEN_STORAGE_FAILED);
        }
    }

    /**
     * Redis에서 리프레시 토큰을 조회합니다.
     *
     * <p>
     * 저장된 리프레시 토큰을 조회하여 반환합니다.
     * </p>
     *
     * @param userId 사용자 ID
     * @return 저장된 리프레시 토큰, 없을 경우 {@code null} 반환
     */
    @Operation(summary = "리프레시 토큰 조회", description = "사용자의 리프레시 토큰을 Redis에서 조회합니다.")
    public String getRefreshToken(int userId) {
        return redisTemplate.opsForValue().get(String.valueOf(userId));
    }

    /**
     * Redis에서 사용자의 리프레시 토큰을 삭제합니다.
     *
     * <p>
     * 로그아웃 또는 보안상의 이유로 특정 사용자의 리프레시 토큰을 삭제합니다.
     * </p>
     *
     * @param userId 사용자 ID
     */
    @Operation(summary = "리프레시 토큰 삭제", description = "사용자의 리프레시 토큰을 Redis에서 삭제합니다.")
    public void deleteRefreshToken(int userId) {
        redisTemplate.delete(String.valueOf(userId));
    }
}

