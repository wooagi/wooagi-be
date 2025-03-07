package com.agarang.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.user.dto.request<br>
 * fileName       : UserUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 젖ㅇ보 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "회원 정보 수정 요청 DTO")
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateRequest {
    @Schema(description = "이름")
    @Size(min = 1, max = 10, message = "이름은 최소 1글자, 최대 10글자 입력 가능합니다.")
    private String name;

    @Schema(description = "이메일")
    @Email
    @Size(max=100)
    private String email;

    @Schema(description = "기존 이미지")
    private String existingImage;
}
