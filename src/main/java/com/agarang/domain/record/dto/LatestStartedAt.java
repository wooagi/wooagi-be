package com.agarang.domain.record.dto;

import com.agarang.domain.record.entity.enumeration.RecordType;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto<br>
 * fileName       : LatestStartedAt.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-17<br>
 * description    : 카테고리별 최근기록 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-17          nature1216          최초생성<br>
 * <br>
 */
public record LatestStartedAt (
    LocalDateTime startedAt,
    RecordType recordType
) {}
