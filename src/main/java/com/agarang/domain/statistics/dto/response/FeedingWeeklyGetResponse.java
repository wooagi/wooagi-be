package com.agarang.domain.statistics.dto.response;

import com.agarang.domain.statistics.dto.ActiveTimeBlock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : FeedingWeeklyGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2/5/25<br>
 * description    : 수유 주간통계 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/5/25          nature1216          최초생성<br>
*/
@Schema(description = "수유 주간통계 응답 DTO")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FeedingWeeklyGetResponse extends WeeklyGetResponse{
    @Schema(description = "평균 수유량")
    private Integer averageAmount;
    @Schema(description = "주로 수유한 시간대")
    private List<ActiveTimeBlock> activeTimeBlocks;
    @Schema(description = "지난주 대비 수유 변화량")
    private BigDecimal weeklyChange;
}
