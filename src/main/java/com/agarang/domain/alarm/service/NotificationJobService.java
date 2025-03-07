package com.agarang.domain.alarm.service;

import com.agarang.domain.alarm.dto.request.NotificationJobRegister;
import com.agarang.domain.alarm.entity.FcmToken;
import com.agarang.domain.alarm.entity.NotificationJob;
import com.agarang.domain.alarm.repository.FcmTokenRepository;
import com.agarang.domain.alarm.repository.NotificationJobRepository;
import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.alarm.service<br>
 * fileName       : NotificationJobService.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  NotificationJob entity 의 Service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.06          Fiat_lux           최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class NotificationJobService {
    private final NotificationJobRepository notificationJobRepository;
    private final BabyRepository babyRepository;
    private final CustodyRepository custodyRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    /**
     * 새로운 알림을 등록합니다.
     *
     * <p>이 메서드는 주어진 아기 ID를 기반으로 알림을 예약합니다.
     * 예약된 시간이 현재 시각보다 이전이면 예외를 발생시킵니다.</p>
     *
     * @param notificationJobRegister 등록할 알림 정보를 담은 요청 객체
     * @throws BusinessException 아기가 존재하지 않거나 잘못된 예약 시간일 경우 예외 발생
     */
    @Transactional
    public void registerNotification(NotificationJobRegister notificationJobRegister) {
        Baby baby = babyRepository.findById(notificationJobRegister.getBabyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));
        if (notificationJobRegister.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.NOTIFICATION_INVALID_SCHEDULED_DATE);
        }

        NotificationJob notificationJob = new NotificationJob(notificationJobRegister.getTitle(), notificationJobRegister.getBody(), notificationJobRegister.getScheduledAt(), baby);
        notificationJobRepository.save(notificationJob);
    }

    /**
     * 예약된 알림 중 아직 전송되지 않은 알림을 조회합니다.
     *
     * @param localDate 현재 시간 기준으로 조회할 예약된 알림의 기준 시간
     * @return 아직 전송되지 않은 알림 목록
     */
    public List<NotificationJob> getPendingNotifications(LocalDateTime localDate) {
        return notificationJobRepository.findByScheduledAtBeforeAndFlagSendFalse(localDate);
    }

    /**
     * 예약된 알림을 처리하고 관련 보호자들에게 FCM 알림을 전송합니다.
     *
     * <p>아기와 연결된 보호자 목록을 조회한 후, 보호자들의 FCM 토큰을 이용해 알림을 전송합니다.
     * 알림이 성공적으로 전송되면 해당 예약된 알림의 `flagSend` 값을 `true`로 변경합니다.</p>
     *
     * @param job 처리할 예약된 알림 객체
     */
    @Transactional
    public void processNotificationJob(NotificationJob job) {
        List<Custody> custodyList = custodyRepository.findByBabyAndDeletedAtIsNull(job.getBaby());
        List<User> custodyUsers = custodyList.stream()
                .map(Custody::getUser)
                .toList();

        List<String> tokens = custodyUsers.stream()
                .flatMap(
                        user -> fcmTokenRepository.findByUser(user)
                                .stream()
                                .map(FcmToken::getFcmToken))
                .distinct()
                .collect(Collectors.toList());

        fcmService.sendNotificationToTokens(tokens, job.getTitle(), job.getBody());
        job.setFlagSend(true);

        notificationJobRepository.save(job);
    }
}
