package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.entity.enumeration.FeedingType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : FeedingGetResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/29/25<br>
 * description    : 수유 기록 조회 응답 베이스 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/29/25          nature1216          최초생성<br>
 */
@Schema(description = "수유 기록 조회 응답 베이스 DTO")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "feeding_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BreastFeedingGetResponse.class, name = "BREAST_FEEDING"),
        @JsonSubTypes.Type(value = FormulaFeedingGetResponse.class, name = "FORMULA_FEEDING"),
        @JsonSubTypes.Type(value = PumpingFeedingGetResponse.class, name = "PUMPING_FEEDING"),
        @JsonSubTypes.Type(value = NormalFeedingGetResponse.class, name = "NORMAL_FEEDING")
})
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class FeedingGetResponse extends BaseRecordGetResponse {
    @Schema(description = "수유 타입")
    private FeedingType feedingType;
}
