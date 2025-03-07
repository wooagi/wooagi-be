package com.agarang.domain.statistics.dto.response;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : GrowthHistoryResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 발육상태 기록 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */

@Schema(description = "발육상태 기록 응답 DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthHistoryResponse {
    @Schema(description = "발육상태 타입")
    private GrowthStatusType growthStatusType;

    @Schema(description = "아기 id")
    private Integer babyId;

    @Schema(description = "발육상태 기록")
    private List<GrowthResponse> histories;

    @Schema(description = "평균 발육상태")
    private List<GrowthAverageResponse> averages;
}
