package com.agarang.domain.statistics.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.pl.NIP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * packageName    : com.agarang.domain.statistics.dto.response<br>
 * fileName       : GrowthResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 발육상태 기록 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */
@Schema(description = "발육상태 기록 조회 응답 DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthResponse {
    @Schema(description = "기록 id")
    private Integer recordId;

    @Schema(description = "측정값")
    private BigDecimal size;

    @Schema(description = "기록일시")
    private LocalDateTime startedAt;

    @Schema(description = "일수")
    private Integer day;

    public GrowthResponse(Integer recordId, BigDecimal size, LocalDateTime startedAt, LocalDateTime birth) {
        this.recordId = recordId;
        this.size = size;
        this.startedAt = startedAt;
        this.day = (int) ChronoUnit.DAYS.between(birth.toLocalDate(), startedAt.toLocalDate());
    }
}
