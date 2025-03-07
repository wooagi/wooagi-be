package com.agarang.domain.record.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : RecordListResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 일자별 기록 목록 조회 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "일자별 기록 목록 조회 응답 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class RecordListResponse {
    @Schema(description = "기준일자")
    LocalDate date;

    @Schema(description = "기록 목록")
    List<BaseRecordGetResponse> records;
}
