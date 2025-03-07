package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.ExcretionColor;
import com.agarang.domain.record.entity.enumeration.ExcretionStatus;
import com.agarang.domain.record.entity.enumeration.ExcretionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : ExcretionUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 배변 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
@Schema(description = "배변 기록 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString(callSuper = true)
public class ExcretionUpdateRequest extends BaseRecordUpdateRequest {
    @Schema(description = "배변 타입")
    private ExcretionType excretionType;

    @Schema(description = "대변 색깔")
    private ExcretionColor color;

    @Schema(description = "대변 상태")
    private ExcretionStatus excretionStatus;
}
