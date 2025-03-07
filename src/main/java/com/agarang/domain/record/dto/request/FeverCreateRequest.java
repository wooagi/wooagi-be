package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.FeverPosition;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : FeverCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/6/25<br>
 * description    : 발열 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/6/25          nature1216          최초생성<br>
 */
@Schema(description = "발열 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeverCreateRequest extends BaseRecordCreateRequest {
    @Schema(description = "체온 측정 위치")
    @Builder.Default
    private FeverPosition position = FeverPosition.EAR;

    @Schema(description = "체온")
    @Builder.Default
    @Digits(integer = 2, fraction = 1)
    private BigDecimal temperature = BigDecimal.valueOf(37.0);
}
