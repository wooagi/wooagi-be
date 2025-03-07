package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.PumpingPosition;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : PumpingUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 유축 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "유축 기록 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString(callSuper = true)
public class PumpingUpdateRequest extends BaseRecordUpdateRequest {
    @Schema(description = "방향")
    @NotNull
    private PumpingPosition position;

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
