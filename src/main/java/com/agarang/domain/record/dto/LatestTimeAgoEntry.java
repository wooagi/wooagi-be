package com.agarang.domain.record.dto;

import com.agarang.domain.record.entity.enumeration.RecordType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * packageName    : com.agarang.domain.record.dto<br>
 * fileName       : LatestTimeAgoEntry.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-17<br>
 * description    : 기록 카테고리별 경과시간 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-17          nature1216          최초생성<br>
 * <br>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LatestTimeAgoEntry (
        RecordType recordType,
        String timeAgo
) {}
