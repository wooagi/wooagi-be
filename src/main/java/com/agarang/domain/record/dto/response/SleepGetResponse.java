package com.agarang.domain.record.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : SleepGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 수면 기록 조회 응답 DTO <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "수면 기록 조회 응답 DTO")
@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SleepGetResponse extends BaseRecordGetResponse {
    @Schema(description = "종료일시")
    private LocalDateTime endedAt;
}
