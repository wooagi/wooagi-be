package com.agarang.domain.growthStandard.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.growthStandard.dto.response<br>
 * fileName       : GrowthStandardResponse.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장 발달 요약 정보 response DTO 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          okeio           최초생성<br>
 * <br>
 */
@Schema(description = "성장 발달 요약 정보 응답 DTO")
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthStandardResponse {
    @Schema(description = "아기의 성장 발달 데이터")
    private String description;
}
