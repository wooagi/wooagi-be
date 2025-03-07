package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : GrowthStatusCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/6/25<br>
 * description    : 발육상태 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/6/25          nature1216          최초생성<br>
 */
@Schema(description = "발육상태 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GrowthStatusCreateRequest extends BaseRecordCreateRequest {
    @Schema(description = "발육상태 타입")
    @NotNull
    private GrowthStatusType growthStatusType;

    @Schema(description = "측정값")
    @NotNull
    @Digits(integer = 3, fraction = 2)
    private BigDecimal size;
}
