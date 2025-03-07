package com.agarang.domain.statistics.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.statistics.dto.ActiveTimeBlock;
import com.agarang.domain.statistics.dto.DailyActiveTime;
import com.agarang.domain.statistics.dto.response.WeeklyGetResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.statistics.service<br>
 * fileName       : WeeklyStatisticsService.java<br>
 * author         : nature1216 <br>
 * date           : 2/4/25<br>
 * description    : 주간통계 서비스 interface입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/4/25          nature1216          최초생성<br>
 */
public interface WeeklyStatisticsService {
    WeeklyGetResponse getWeeklyStatistics(LocalDate date, Integer userId, Integer babyId);
    List<DailyActiveTime> getActiveTimes(LocalDate date, Baby baby);
    BigDecimal getAverageCount(LocalDate date, Baby baby);
    RecordType getRecordType();
    List<ActiveTimeBlock> analyzePattern(LocalDate date, Baby baby);
}
