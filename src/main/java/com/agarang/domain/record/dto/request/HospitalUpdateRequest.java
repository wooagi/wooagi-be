package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.HospitalVisitType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : HospitalUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 병원 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "병원 기록 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString(callSuper = true)
public class HospitalUpdateRequest extends BaseRecordUpdateRequest {
    @Schema(description = "방문목적 타입")
    private HospitalVisitType visitType;
}
