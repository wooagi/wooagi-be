package com.agarang.domain.statistics.dto.response;

import com.agarang.domain.record.entity.enumeration.ExcretionColor;
import com.agarang.domain.record.entity.enumeration.ExcretionStatus;
import com.agarang.domain.statistics.dto.ActiveTimeBlock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : ExcretionWeeklyGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2/5/25<br>
 * description    : 배변 주간통계 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/5/25          nature1216          최초생성<br>
 */

@Schema(description = "배변 주간통계 응답 DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExcretionWeeklyGetResponse extends WeeklyGetResponse {
    @Schema(description = "비정상 배변횟수")
    private Integer abnormalCount;
    @Schema(description = "배변 색깔별 횟수")
    private Map<ExcretionColor, Integer> colorCount;
    @Schema(description = "배변 상태별 횟수")
    private Map<ExcretionStatus, Integer> statusCount;
    @Schema(description = "주로 배변한 시간대")
    private List<ActiveTimeBlock> activeTimeBlocks;
}
