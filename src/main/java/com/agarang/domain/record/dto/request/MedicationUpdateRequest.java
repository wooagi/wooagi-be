package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.AntipyreticType;
import com.agarang.domain.record.entity.enumeration.MedicationType;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : MedicationUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 복약 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "복약 기록 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MedicationUpdateRequest extends BaseRecordUpdateRequest {
    @Schema(description = "약 타입")
    private MedicationType medicationType;

    @Schema(description = "해열제 성분")
    private AntipyreticType specificType;

    @Schema(description = "투약량")
    @Digits(integer = 2, fraction = 1)
    private BigDecimal amount;

    public void validate() {
        if(medicationType == null) {
            throw new BusinessException(ErrorCode.INVALID_MODIFY_REQUEST);
        }

        switch (medicationType) {
            case ANTIPYRETIC:
                if(specificType == null || amount == null) {
                    throw new BusinessException(ErrorCode.INVALID_MODIFY_REQUEST);
                }
        }
    }
}
