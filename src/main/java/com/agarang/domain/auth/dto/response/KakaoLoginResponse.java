package com.agarang.domain.auth.dto.response;

import com.agarang.domain.auth.dto.LoginUserInfo;
import com.agarang.domain.user.dto.response.UserResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * packageName    : com.agarang.domain.auth.dto.response<br>
 * fileName       : KakaoLoginResponse.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 22.<br>
 * description    : 카카오 로그인 요청을 처리하는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          okeio           최초 생성<br>
 */
@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoLoginResponse {
    private String accessToken;
    private String refreshToken;
    private LoginUserInfo user;
}
