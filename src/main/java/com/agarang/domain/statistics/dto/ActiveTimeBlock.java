package com.agarang.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

/**
 * packageName    : com.agarang.domain.statistics.dto<br>
 * fileName       : ActiveTimeBlock.java<br>
 * author         : nature1216 <br>
 * date           : 2/5/25<br>
 * description    : 기록의 기록된 시간대 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/5/25          nature1216          최초생성<br>
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ActiveTimeBlock {
    private LocalTime start;
    private LocalTime end;

    public boolean isConnected(ActiveTimeBlock other) {
        return this.end.equals(other.start);
    }

    public ActiveTimeBlock merge(ActiveTimeBlock other) {
        return new ActiveTimeBlock(this.start, other.end);
    }
}
