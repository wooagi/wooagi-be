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
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : FeedingUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 1/29/25<br>
 * description    : 수유 기록 수정 요청 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/29/25          nature1216          최초생성<br>
 */
@Schema(description = "수유 기록 수정 요청 DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeedingUpdateRequest extends BaseRecordUpdateRequest {
    @Schema(description = "수유 타입")
    private FeedingType feedingType;

    @Schema(description = "수유량")
    @Min(0) @Max(32767)
    private Integer amount;

    @Schema(description = "모유수유 방향")
    private BreastFeedingPosition position;

    @Schema(description = "왼쪽 수유시간")
    @Min(0) @Max(127)
    private Integer leftTime;

    @Schema(description = "오른쪽 수유시간")
    @Min(0) @Max(127)
    private Integer rightTime;

    @Schema(description = "총 수유시간")
    @Min(0) @Max(32767)
    private Integer totalTime;

    public void validate() {
        if(feedingType == null) {
            throw new BusinessException(ErrorCode.INVALID_MODIFY_REQUEST);
        }

        if (feedingType == FeedingType.BREAST_FEEDING) {
            if (position == null || leftTime == null || rightTime == null || totalTime == null) {
                throw new BusinessException(ErrorCode.INVALID_MODIFY_REQUEST);
            }
        } else {
            if (amount == null) {
                throw new BusinessException(ErrorCode.INVALID_MODIFY_REQUEST);
            }
        }
    }
}
