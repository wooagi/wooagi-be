package com.agarang.domain.alarm.event;

import com.agarang.domain.alarm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * packageName    : com.agarang.domain.alarm.event<br>
 * fileName       : FcmTokenEventListener.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 5.<br>
 * description    :  Fcm token event listener 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.05          Fiat_lux           최초생성<br>
 */
@Component
@RequiredArgsConstructor
public class FcmTokenEventListener {
    private final FcmService fcmService;

    /**
     * 트랜잭션이 성공적으로 커밋된 후, FCM 알림을 전송하는 이벤트를 처리합니다.
     *
     * <p>이벤트가 발생하면 {@link FcmService#sendNotificationToManyToken} 메서드를 호출하여
     * 다수의 토큰을 대상으로 알림을 전송합니다.</p>
     *
     * @param fcmTokenSendMessageEvent FCM 알림을 전송할 이벤트 객체
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotificationEvent(FcmTokenSendMessageEvent fcmTokenSendMessageEvent) {
        fcmService.sendNotificationToManyToken(fcmTokenSendMessageEvent.getBabyId(), fcmTokenSendMessageEvent.getTitle(), fcmTokenSendMessageEvent.getMessage());
    }

    /**
     * 트랜잭션과 무관하게 즉시 실행되는 FCM 알림 전송 이벤트를 처리합니다.
     *
     * <p>이벤트가 발생하면 {@link FcmService#sendNotificationToManyToken} 메서드를 호출하여
     * 다수의 토큰을 대상으로 알림을 전송합니다.</p>
     *
     * @param fcmTokenSendMessageEvent FCM 알림을 전송할 이벤트 객체
     */
    @EventListener
    public void handleNotificationEventNoTransaction(FcmTokenSendMessageEvent fcmTokenSendMessageEvent) {
        fcmService.sendNotificationToManyToken(fcmTokenSendMessageEvent.getBabyId(), fcmTokenSendMessageEvent.getTitle(), fcmTokenSendMessageEvent.getMessage());
    }
}
