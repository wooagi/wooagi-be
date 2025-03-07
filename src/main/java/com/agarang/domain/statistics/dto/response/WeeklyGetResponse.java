package com.agarang.domain.statistics.dto.response;

import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.statistics.dto.DailyActiveTime;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : WeeklyGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2/5/25<br>
 * description    : 주간통계 응답 베이스 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/5/25          nature1216          최초생성<br>
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "record_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExcretionWeeklyGetResponse.class, name = "EXCRETION"),
        @JsonSubTypes.Type(value = SleepWeeklyGetResponse.class, name = "SLEEP"),
        @JsonSubTypes.Type(value = FeedingWeeklyGetResponse.class, name = "FEEDING")
})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Data
@SuperBuilder
public abstract class WeeklyGetResponse {
    @Schema(description = "기준일자")
    private LocalDate targetDate;
    @Schema(description = "기록 카테고리")
    private RecordType recordType;
    @Schema(description = "평균횟수")
    private BigDecimal averageCount;
    @Schema(description = "기록시간대")
    private List<DailyActiveTime> activeTimes;
}
