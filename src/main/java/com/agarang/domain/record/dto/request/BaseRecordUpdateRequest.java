package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.RecordType;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : BaseRecordUpdateRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-23<br>
 * description    : 기록 수정 요청 베이스 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-23          nature1216          최초생성<br>
 * <br>
 */

@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "record_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BathUpdateRequest.class, name = "BATH"),
        @JsonSubTypes.Type(value = ClipUpdateRequest.class, name = "CLIP"),
        @JsonSubTypes.Type(value = ExcretionUpdateRequest.class, name = "EXCRETION"),
        @JsonSubTypes.Type(value = FeedingUpdateRequest.class, name = "FEEDING"),
        @JsonSubTypes.Type(value = FeverUpdateRequest.class, name = "FEVER"),
        @JsonSubTypes.Type(value = GrowthStatusUpdateRequest.class, name = "GROWTH_STATUS"),
        @JsonSubTypes.Type(value = HospitalUpdateRequest.class, name = "HOSPITAL"),
        @JsonSubTypes.Type(value = MedicationUpdateRequest.class, name = "MEDICATION"),
        @JsonSubTypes.Type(value = PumpingUpdateRequest.class, name = "PUMPING"),
        @JsonSubTypes.Type(value = SleepUpdateRequest.class, name = "SLEEP"),
        @JsonSubTypes.Type(value = SolidFoodUpdateRequest.class, name = "SOLID_FOOD")
})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Setter
@ToString
public abstract class BaseRecordUpdateRequest {
    @Schema(description = "기록 카테고리")
    private RecordType recordType;

    @Schema(description = "메모")
    private String content;

    @Schema(description = "기존 메모 이미지")
    private String existingImage;

    @Schema(description = "기록일시")
    private LocalDateTime startedAt;
}
