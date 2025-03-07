package com.agarang.domain.statistics.dto.response;

import com.agarang.domain.statistics.dto.ActiveTimeBlock;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : SleepWeeklyGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2/4/25<br>
 * description    : 수면 주간통계 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/4/25          nature1216          최초생성<br>
 */
@Schema(description = "수면 주간통계 응답 DTO")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@SuperBuilder
public class SleepWeeklyGetResponse extends WeeklyGetResponse {
    @Schema(description = "평균 수면시간")
    private BigDecimal averageHour;
    @Schema(description = "주로 수면한 시간대")
    private List<ActiveTimeBlock> activeTimeBlocks;
    @Schema(description = "지난주 대비 수면시간 변화량")
    private BigDecimal weeklyChange;
}
