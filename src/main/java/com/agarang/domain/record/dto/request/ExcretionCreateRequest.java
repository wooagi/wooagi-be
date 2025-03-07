package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.ExcretionColor;
import com.agarang.domain.record.entity.enumeration.ExcretionStatus;
import com.agarang.domain.record.entity.enumeration.ExcretionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : ExcretionCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/6/25<br>
 * description    : 배변 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/6/25          nature1216          최초생성<br>
 */
@Schema(description = "배변 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExcretionCreateRequest extends BaseRecordCreateRequest {
    @Builder.Default
    private ExcretionType excretionType = ExcretionType.PEE;
    @Schema(description = "배변 색깔")
    private ExcretionColor color;
    @Schema(description = "배변 상태")
    private ExcretionStatus excretionStatus;
}
