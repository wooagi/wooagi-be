package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.AntipyreticType;
import com.agarang.domain.record.entity.enumeration.MedicationType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : MedicationCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 복약 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "복약 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MedicationCreateRequest extends BaseRecordCreateRequest {
    @Schema(description = "약 타입")
    @Builder.Default
    private MedicationType medicationType = MedicationType.OTHERS;

    @Schema(description = "해열제 성분")
    private AntipyreticType specificType;

    @Schema(description = "투약량")
    @Digits(integer = 2, fraction = 1)
    private BigDecimal amount;
}
