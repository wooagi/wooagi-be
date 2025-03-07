package com.agarang.domain.record.dto.response;

import com.agarang.domain.record.dto.LatestTimeAgoEntry;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : LatestTimeAgoResponse.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-17<br>
 * description    : 카테고리별 기록 경과시간 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-17          nature1216          최초생성<br>
 * <br>
 */
@Schema(description = "카테고리별 기록 경과시간 응담 DTO")
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LatestTimeAgoResponse (
        @Schema(description = "카테고리별 경과시간 리스트")
        List<LatestTimeAgoEntry> data
) {}
