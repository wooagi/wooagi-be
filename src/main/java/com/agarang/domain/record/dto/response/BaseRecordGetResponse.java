package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.dto.request.*;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : null.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-23<br>
 * description    : 기록 조회 응답 베이스 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-23          nature1216          최초생성<br>
 * <br>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "record_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BathGetResponse.class, name = "BATH"),
        @JsonSubTypes.Type(value = ClipGetResponse.class, name = "CLIP"),
        @JsonSubTypes.Type(value = ExcretionGetResponse.class, name = "EXCRETION"),
        @JsonSubTypes.Type(value = FeedingGetResponse.class, name = "FEEDING"),
        @JsonSubTypes.Type(value = FeverGetResponse.class, name = "FEVER"),
        @JsonSubTypes.Type(value = GrowthStatusGetResponse.class, name = "GROWTH_STATUS"),
        @JsonSubTypes.Type(value = HospitalGetResponse.class, name = "HOSPITAL"),
        @JsonSubTypes.Type(value = MedicationGetResponse.class, name = "MEDICATION"),
        @JsonSubTypes.Type(value = PumpingUpdateRequest.class, name = "PUMPING"),
        @JsonSubTypes.Type(value = SleepGetResponse.class, name = "SLEEP"),
        @JsonSubTypes.Type(value = SolidFoodGetResponse.class, name = "SOLID_FOOD")
})
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class BaseRecordGetResponse {
    @Schema(description = "기록 id")
    private Integer recordId;

    @Schema(description = "기록 메모 이미지")
    private String recordImage;

    @Schema(description = "메모")
    private String content;

    @Schema(description = "기록일시")
    private LocalDateTime startedAt;

    @Schema(description = "기록 카테고리")
    private RecordType recordType;
}
