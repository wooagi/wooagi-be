package com.agarang.domain.statistics.dto;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.statistics.dto<br>
 * fileName       : GrowthRecord.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 성장 기록 데이터를 저장하는 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthRecord {
    private Integer recordId;
    private GrowthStatusType type;
    private BigDecimal size;
    private LocalDateTime startedAt;
}
