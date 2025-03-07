package com.agarang.domain.growthStandard.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.growthStandard.dto.response<br>
 * fileName       : GrowthStandardItemResponse.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 25.<br>
 * description    : 성장 발달 데이터의 개별 항목을 표현하기 위한 DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.25        okeio           최초생성<br>
 * <br>
 */
@Schema(description = "성장 발달 정보 DTO")
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthStandardItemResponse {
    private String description;
    private String growthType;
}

