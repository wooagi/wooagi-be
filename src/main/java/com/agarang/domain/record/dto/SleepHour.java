package com.agarang.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto<br>
 * fileName       : SleepHour.java<br>
 * author         : nature1216 <br>
 * date           : 2/7/25<br>
 * description    : 수면시간 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/7/25          nature1216          최초생성<br>
 */
@Data
@AllArgsConstructor
@ToString
public class SleepHour {
    private LocalDateTime start;
    private LocalDateTime end;
}
