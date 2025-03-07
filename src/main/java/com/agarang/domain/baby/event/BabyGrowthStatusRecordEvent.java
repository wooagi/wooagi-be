package com.agarang.domain.baby.event;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.baby.event<br>
 * fileName       : BabyGrowthStatusRecordEvent.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 04.<br>
 * description    :아기의 성장 상태 기록 이벤트 클래스입니다.
 * <p>이 이벤트는 아기의 성장 정보를 기록할 때 발생하며,
 * Spring의 {@link ApplicationEvent}를 확장하여 사용됩니다.</p> <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.24          Fiat_lux           최초생성<br>
 */
@Getter
public class BabyGrowthStatusRecordEvent extends ApplicationEvent {
    private final Integer babyId;
    private final GrowthStatusType growthStatus;
    private final BigDecimal size;

    /**
     * {@code BabyGrowthStatusRecordEvent} 생성자입니다.
     *
     * @param source       이벤트 발생 객체
     * @param babyId       성장 상태가 기록된 아기의 ID
     * @param growthStatus 성장 상태 유형 {@link GrowthStatusType}
     * @param size         성장 상태의 수치 (예: 몸무게, 키 등)
     */
    public BabyGrowthStatusRecordEvent(Object source, Integer babyId, GrowthStatusType growthStatus, BigDecimal size) {
        super(source);
        this.babyId = babyId;
        this.growthStatus = growthStatus;
        this.size = size;
    }
}
