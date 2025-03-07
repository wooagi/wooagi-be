package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.AntipyreticType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : AntipyreticCheckRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/9/25<br>
 * description    : 해열제 복용 가능 여부 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/9/25          nature1216          최초생성<br>
 */
@Schema(description = "해열제 복용 가능 여부 체크 요청 DTO")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AntipyreticCheckRequest (
        @Schema(description = "복용량")
        @NotNull AntipyreticType antipyreticType,
        @NotNull @Digits(integer = 2, fraction = 1)
        BigDecimal amount,
        @Schema(description = "기록 id")
        Integer recordId
) {}
