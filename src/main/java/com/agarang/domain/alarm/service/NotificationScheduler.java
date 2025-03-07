package com.agarang.domain.alarm.service;

import com.agarang.domain.alarm.entity.FcmToken;
import com.agarang.domain.alarm.entity.NotificationJob;
import com.agarang.domain.alarm.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.agarang.domain.alarm.service<br>
 * fileName       : NotificationScheduler.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  Notification 스케줄러 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.06          Fiat_lux           최초생성<br>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationJobService notificationJobService;

    /**
     * 6개월 이상 사용되지 않은 FCM 토큰을 정리하는 작업을 수행합니다.
     *
     * <p>이 작업은 매일 자정(00:00:00)에 실행되며, `updatedAt` 값이 6개월 이상 지난
     * FCM 토큰을 찾아 삭제합니다.</p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldTokens() {
        LocalDate thresholdDate = LocalDate.now().minusMonths(6);
        List<FcmToken> oldTokens = fcmTokenRepository.findByUpdatedAtBefore(thresholdDate);
        if (!oldTokens.isEmpty()) {
            fcmTokenRepository.deleteAll(oldTokens);
        }
    }

    /**
     * 예약된 알림을 처리하는 작업을 수행합니다.
     *
     * <p>이 작업은 매 분마다 실행되며, 현재 시각 이전에 예약된 알림을 찾아 FCM을 통해 전송합니다.</p>
     */
    @Scheduled(cron = "0 * * * * *")
    public void processScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        List<NotificationJob> pendingNotifications = notificationJobService.getPendingNotifications(now);
        log.info("========================== " + now.toString() + " ==========================");
        log.info("대기중인 알림 건수: " + pendingNotifications.size());
        for (NotificationJob notificationJob : pendingNotifications) {
            notificationJobService.processNotificationJob(notificationJob);
        }
    }
}
