package com.agarang.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * packageName    : com.agarang.domain.user.dto.request<br>
 * fileName       : UserReactivateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-19<br>
 * description    : 탈퇴회원 데이터 복구 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-19          nature1216          최초생성<br>
 * <br>
 */
@Schema(description = "탈퇴회원 데이터 복구 요청 DTO입니다.")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserReactivateRequest (
        @Schema(description = "데이터 복구 여부")
        @NotNull boolean reactivate
) {}
