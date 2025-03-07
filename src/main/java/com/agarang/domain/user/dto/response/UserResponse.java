package com.agarang.domain.user.dto.response;

import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.agarang.domain.user.dto.response<br>
 * fileName       : UserResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 정보 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "회원 정보 조회 응답 DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    @Schema(description = "회원 id")
    private int userId;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "프로필 이미지 url")
    private String userImage;

    @Schema(description = "연결된 아기 수")
    private int babyCount;

    @Schema(description = "성별")
    private Sex sex;
}
