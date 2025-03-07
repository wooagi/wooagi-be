package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.entity.enumeration.AntipyreticType;
import com.agarang.domain.record.entity.enumeration.MedicationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : MedicationGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 복약 기록 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "복약 기록 조회 응답 DTO")
@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MedicationGetResponse extends BaseRecordGetResponse {
    @Schema(description = "약 타입")
    private MedicationType medicationType;

    @Schema(description = "해열제 성분")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AntipyreticType specificType;

    @Schema(description = "해열제 복용량")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal amount;
}
