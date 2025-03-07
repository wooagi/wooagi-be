package com.agarang.domain.record.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : ClipListResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "일자별 클립 기록 조회 응답 DTO")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@ToString
public class ClipListResponse {
    @Schema(description = "기준일자")
    private LocalDate date;

    @Schema(description = "클립 리스트")
    private List<ClipGetResponse> clips;
}
