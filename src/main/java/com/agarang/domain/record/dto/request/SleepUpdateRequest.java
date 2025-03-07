package com.agarang.domain.record.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : SleepUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 수면 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "수면 기록 수정 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString(callSuper = true)
public class SleepUpdateRequest extends BaseRecordUpdateRequest {
    @Schema(description = "종료일시")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endedAt;
}



