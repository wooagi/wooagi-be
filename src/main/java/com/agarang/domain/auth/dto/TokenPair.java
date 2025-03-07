package com.agarang.domain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.auth.dto<br>
 * fileName       : TokenPair.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 29.<br>
 * description    : 회원가입 및 로그인 시 토큰을 담는 DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.29          okeio           최초생성<br>
 * <br>
 */
@Getter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "회원가입 및 로그인 시 토큰을 담는 DTO")
public class TokenPair {
    @Schema(description = "액세스 토큰 (JWT 형식)", example = "eyJhbGciOi...")
    private String accessToken;
    @Schema(description = "리프레시 토큰 (JWT 형식)", example = "eyJhbGciOi....")
    private String refreshToken;
}
