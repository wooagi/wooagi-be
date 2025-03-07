package com.agarang.domain.alarm.repository;

import com.agarang.domain.alarm.entity.NotificationJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.agarang.domain.alarm.repository<br>
 * fileName       : NotificationJobRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  NotificationJob entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.06           Fiat_lux         최초생성<br>
 * <br>
 */
public interface NotificationJobRepository extends JpaRepository<NotificationJob, Integer> {

    /**
     * 지정된 시간 이전에 예약되었으며 아직 전송되지 않은 알림 작업을 조회합니다.
     *
     * <p>이 메서드는 `scheduledAt`이 특정 시간 이전이며, `flagSend` 값이 `false`인
     * 알림 작업을 검색하는 데 사용됩니다.</p>
     *
     * @param now 현재 시각 (이 시간 이전에 예약된 알림을 조회)
     * @return 아직 전송되지 않은 알림 작업 리스트
     */
    List<NotificationJob> findByScheduledAtBeforeAndFlagSendFalse(LocalDateTime now);
}
