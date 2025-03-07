package com.agarang.domain.auth.dto;

import com.agarang.domain.user.dto.response.UserResponse;
import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.agarang.domain.auth.dto<br>
 * fileName       : LoginUserInfo.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-19<br>
 * description    : 로그인한 회원 정보 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-19          nature1216          최초생성<br>
 * <br>
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginUserInfo extends UserResponse {
    private boolean canActivate;
}
