package com.agarang.domain.record.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : SleepCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/6/25<br>
 * description    : 수면 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/6/25          nature1216          최초생성<br>
 */
@Schema(description = "수면 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SleepCreateRequest extends BaseRecordCreateRequest {
    @Schema(description = "종료일시")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endedAt;
}
