package com.agarang.domain.statistics.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * packageName    : com.agarang.domain.statistics.dto<br>
 * fileName       : ActiveTime.java<br>
 * author         : nature1216 <br>
 * date           : 2/4/25<br>
 * description    : 주간패턴 그래프에 사용되는 active time DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/4/25          nature1216          최초생성<br>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActiveTime {
    private Integer start;
    private Integer end;
}
