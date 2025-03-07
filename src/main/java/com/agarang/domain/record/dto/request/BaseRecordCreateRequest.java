package com.agarang.domain.record.dto.request;

import com.agarang.domain.record.entity.enumeration.RecordType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto.request<br>
 * fileName       : CreateRecordRequest.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-22<br>
 * description    : 기록 생성 요청 베이스 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-22          nature1216          최초생성<br>
 * <br>
 */
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "record_type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BathCreateRequest.class, name = "BATH"),
        @JsonSubTypes.Type(value = ClipCreateRequest.class, name = "CLIP"),
        @JsonSubTypes.Type(value = ExcretionCreateRequest.class, name = "EXCRETION"),
        @JsonSubTypes.Type(value = FeedingCreateRequest.class, name = "FEEDING"),
        @JsonSubTypes.Type(value = FeverCreateRequest.class, name = "FEVER"),
        @JsonSubTypes.Type(value = GrowthStatusCreateRequest.class, name = "GROWTH_STATUS"),
        @JsonSubTypes.Type(value = HospitalCreateRequest.class, name = "HOSPITAL"),
        @JsonSubTypes.Type(value = MedicationCreateRequest.class, name = "MEDICATION"),
        @JsonSubTypes.Type(value = PumpingCreateRequest.class, name = "PUMPING"),
        @JsonSubTypes.Type(value = SleepCreateRequest.class, name = "SLEEP"),
        @JsonSubTypes.Type(value = SolidFoodCreateRequest.class, name = "SOLID_FOOD")
})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class BaseRecordCreateRequest {
    @Schema(description = "기록 카테고리")
    @NotNull
    private RecordType recordType;

    @Schema(description = "기록일시")
    @NotNull
    private LocalDateTime startedAt;

    @Schema(description = "메모")
    private String content;
}
