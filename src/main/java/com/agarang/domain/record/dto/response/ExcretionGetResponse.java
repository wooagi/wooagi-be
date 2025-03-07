package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.entity.enumeration.ExcretionColor;
import com.agarang.domain.record.entity.enumeration.ExcretionStatus;
import com.agarang.domain.record.entity.enumeration.ExcretionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : ExcretionGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 배변 기록 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
@Schema(description = "배변 기록 조회 응답 DTO")
@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExcretionGetResponse extends BaseRecordGetResponse {
    @Schema(description = "배변 타입")
    private ExcretionType excretionType;

    @Schema(description = "대변 색")
    private ExcretionColor color;

    @Schema(description = "대변 상태")
    private ExcretionStatus excretionStatus;
}
