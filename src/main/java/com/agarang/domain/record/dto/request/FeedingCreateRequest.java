package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.BreastFeedingPosition;
import com.agarang.domain.record.entity.enumeration.FeedingType;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : FeedingCreateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2/6/25<br>
 * description    : 수유 기록 추가 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/6/25          nature1216          최초생성<br>
 */
@Schema(description = "수유 기록 추가 요청 DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeedingCreateRequest extends BaseRecordCreateRequest {
    @Schema(description = "수유 타입")
    @Builder.Default
    private FeedingType feedingType = FeedingType.NORMAL_FEEDING;

    @Schema(description = "수유량")
    @Min(0) @Max(32767)
    private Integer amount;

    @Schema(description = "모유수유 방향")
    @Builder.Default
    private BreastFeedingPosition position = BreastFeedingPosition.UNKNOWN;

    @Schema(description = "왼쪽 수유시간")
    @Min(0) @Max(127)
    @Builder.Default
    private Integer leftTime = 0;

    @Schema(description = "오른쪽 수유시간")
    @Min(0) @Max(127)
    @Builder.Default
    private Integer rightTime = 0;

    @Schema(description = "총 수유시간")
    @Min(0) @Max(32767)
    @Builder.Default
    private Integer totalTime = 0;
}
