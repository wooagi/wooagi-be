package com.agarang.domain.statistics.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : nulGrowthStatisticsResponsel.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 발육상태 통계 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */

@Schema(description = "발육상태 통계 조회 응답 DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthStatisticsResponse {
    @Schema(description = "최근 기록 대비 변화량")
    private BigDecimal growthChange;
    @Schema(description = "평균 기준 등수")
    private Integer rank;
}
