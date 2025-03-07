package com.agarang.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.auth.dto.request<br>
 * fileName       : RefreshTokenRequest.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 29.<br>
 * description    : 액세스 토큰 재발급을 위한 request DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.29          okeio           최초생성<br>
 * <br>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "액세스 토큰 재발급을 위한 요청 DTO")
public class RefreshTokenRequest {
    @NotBlank
    @JsonProperty("refresh_token")
    @Schema(description = "리프레시 토큰 (JWT 형식)", example = "dGhpcyBpcy...")
    private String refreshToken;
}