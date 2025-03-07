package com.agarang.domain.record.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : RecordCreateResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-31<br>
 * description    : 기록 추가 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-31          nature1216          최초생성<br>
 * <br>
 */

@Schema(description = "기록 추가 응답 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class RecordCreateResponse {
    @Schema(description = "기록 id")
    private Integer recordId;
}
