package com.agarang.domain.statistics.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : GrowthAverageResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 평균 발육상태 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */
@Schema(description = "평균 발육상태 응답 DTO")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthAverageResponse {
    @Schema(description = "일수")
    private Integer day;

    @Schema(description = "측정값")
    private BigDecimal size;
}
