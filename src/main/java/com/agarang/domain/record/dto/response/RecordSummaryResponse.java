package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.entity.enumeration.RecordType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : RecordSummaryResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 최근기록 경과시간 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "최근기록 경과시간 조회 응답 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class RecordSummaryResponse {
    @Schema(description = "기준일자")
    private LocalDate date;

    @Schema(description = "카테고리별 경과시간 리스트")
    private Map<RecordType, Object> summary;
}
