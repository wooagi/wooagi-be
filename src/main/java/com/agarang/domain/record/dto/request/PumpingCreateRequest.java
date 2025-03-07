package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.PumpingPosition;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : PumpingCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/6/25<br>
 * description    : 유축 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/6/25          nature1216          최초생성<br>
 */
@Schema(description = "유축 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PumpingCreateRequest extends BaseRecordCreateRequest {
    @Schema(description = "방향")
    @Builder.Default
    private PumpingPosition position = PumpingPosition.UNKNOWN;

    @Schema(description = "오른쪽 유축량")
    @Min(0) @Max(32767)
    private Integer rightAmount;

    @Schema(description = "왼쪽 유축량")
    @Min(0) @Max(32767)
    private Integer leftAmount;

    @Schema(description = "총 유축량")
    @Min(0) @Max(32767)
    private Integer totalAmount;
}
