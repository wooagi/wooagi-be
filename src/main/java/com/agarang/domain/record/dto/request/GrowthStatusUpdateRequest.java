package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : GrowthStatusUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 발육상태 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "발육상태 기록 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString(callSuper = true)
public class GrowthStatusUpdateRequest extends BathUpdateRequest {
    @Schema(description = "발육상태 타입")
    private GrowthStatusType growthStatusType;

    @Schema(description = "측정값")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal size;
}
