package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.entity.enumeration.FeverPosition;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : FeverGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 열 기록 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "열 기록 조회 응답 DTO")
@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeverGetResponse extends BaseRecordGetResponse {
    @Schema(description = "측정위치")
    private FeverPosition position;

    @Schema(description = "체온")
    private BigDecimal temperature;
}
