package com.agarang.domain.growthStandard.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.agarang.domain.growthStandard.dto.response<br>
 * fileName       : DetailGrowthStandardResponse.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장 발달 상세 정보 response DTO 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          okeio           최초생성<br>
 * <br>
 */
@Schema(description = "성장 발달 상세 정보 응답 DTO")
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class DetailGrowthStandardResponse {
    @Schema(description = "아기의 성장 발달 데이터 목록")
    private List<GrowthStandardItemResponse> babyData;
    @Schema(description = "어머니가 알면 좋을 아기의 성장 발달 데이터")
    private String motherData;
    @Schema(description = "아버지가 알면 좋을 아기의 성장 발달 데이터")
    private String fatherData;
}
