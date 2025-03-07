package com.agarang.domain.baby.event;

import com.agarang.domain.baby.service.BabyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * packageName    : com.agarang.domain.baby.event<br>
 * fileName       : BabyGrowthStatusRecordEventListener.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 04.<br>
 * description    : 아기의 성장 상태 기록 이벤트를 처리하는 리스너 클래스입니다.
 * <p>이 클래스는 {@link BabyGrowthStatusRecordEvent} 이벤트를 구독하며,
 * 트랜잭션이 커밋되기 전에 아기의 성장 상태를 업데이트합니다.</p>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.24          Fiat_lux           최초생성<br>
 */
@Component
@RequiredArgsConstructor
public class BabyGrowthStatusRecordEventListener {
    private final BabyService babyService;

    /**
     * 아기의 성장 상태 기록 이벤트를 처리합니다.
     *
     * <p>이 메서드는 트랜잭션 커밋 전에 실행되며,
     * {@link BabyService#updateGrowthStatus} 메서드를 호출하여 성장 상태를 업데이트합니다.</p>
     *
     * @param event 아기의 성장 상태 변경 이벤트 {@link BabyGrowthStatusRecordEvent}
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleBabyGrowthStatusRecordEvent(BabyGrowthStatusRecordEvent event) {
        babyService.updateGrowthStatus(event.getBabyId(), event.getGrowthStatus(), event.getSize());
    }
}
