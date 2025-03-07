package com.agarang.domain.statistics.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.statistics.dto<br>
 * fileName       : DailyActiveTime.java<br>
 * author         : nature1216 <br>
 * date           : 2/5/25<br>
 * description    : 일자별 Active time DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/5/25          nature1216          최초생성<br>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DailyActiveTime {
    LocalDate date;
    List<ActiveTime> dailyActiveTimes;
}
