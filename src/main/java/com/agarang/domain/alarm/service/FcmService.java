package com.agarang.domain.alarm.service;

import com.agarang.domain.alarm.dto.request.FcmTokenRequest;
import com.agarang.domain.alarm.entity.FcmToken;
import com.agarang.domain.alarm.repository.FcmTokenRepository;
import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.alarm.service<br>
 * fileName       : FcmService.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 11.<br>
 * description    :  FCM(Firebase Cloud Messaging) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 이 클래스는 FCM 토큰을 저장 및 삭제하고, 특정 사용자 또는 다수의 사용자에게 알림을 전송하는 기능을 제공합니다.<br>
 * <p>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.11           Fiat_lux         최초생성<br>
 * <br>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final BabyRepository babyRepository;
    private final CustodyRepository custodyRepository;

    /**
     * 사용자의 FCM 토큰을 저장합니다.
     *
     * <p>이미 저장된 토큰이 있을 경우, 업데이트 시간을 갱신합니다.</p>
     *
     * @param userId          FCM 토큰을 등록할 사용자 ID
     * @param fcmTokenRequest 등록할 FCM 토큰 정보를 포함한 요청 객체
     */
    @Transactional
    public void saveToken(Integer userId, FcmTokenRequest fcmTokenRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String token = fcmTokenRequest.getToken();
        Optional<FcmToken> existingTokenOpt = fcmTokenRepository.findByUserAndFcmToken(user, token);

        if (existingTokenOpt.isPresent()) {
            existingTokenOpt.get().setUpdatedAt(LocalDate.now());
        } else {
            fcmTokenRepository.save(new FcmToken(token, user));
        }
    }

    /**
     * 사용자의 특정 FCM 토큰을 삭제합니다.
     *
     * @param userId          삭제할 FCM 토큰을 소유한 사용자 ID
     * @param fcmTokenRequest 삭제할 FCM 토큰 정보를 포함한 요청 객체
     */
    @Transactional
    public void deleteToken(Integer userId, FcmTokenRequest fcmTokenRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Optional<FcmToken> existingTokenOpt = fcmTokenRepository.findByUserAndFcmToken(user, fcmTokenRequest.getToken());
        existingTokenOpt.ifPresent(fcmTokenRepository::delete);
    }

    /**
     * 여러 FCM 토큰에 알림을 전송합니다.
     *
     * <p>전송 중 `UNREGISTERED` 오류가 발생하면 해당 토큰을 삭제합니다.</p>
     *
     * @param tokens 알림을 보낼 FCM 토큰 목록
     * @param title  알림 제목
     * @param body   알림 내용
     */
    @Transactional
    public void sendNotificationToTokens(List<String> tokens, String title, String body) {
        if (Objects.isNull(tokens) || tokens.isEmpty()) {
            return;
        }

        for (String token : tokens) {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(token)
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                if (MessagingErrorCode.UNREGISTERED.equals(e.getMessagingErrorCode())) {
                    fcmTokenRepository.findByFcmToken(token).ifPresent(fcm -> fcmTokenRepository.deleteByFcmToken(token));
                }
            }
        }
    }

    /**
     * 특정 아기와 관련된 모든 보호자의 FCM 토큰에 알림을 전송합니다.
     *
     * <p>해당 아기와 보호자 관계를 조회하여, 보호자의 모든 FCM 토큰을 찾아 알림을 전송합니다.</p>
     *
     * @param babyId 알림을 보낼 대상 아기의 ID
     * @param title  알림 제목
     * @param body   알림 내용
     */
    @Transactional
    public void sendNotificationToManyToken(Integer babyId, String title, String body) {
        Baby baby = babyRepository.findById(babyId).orElseThrow();

        List<Custody> custodyList = custodyRepository.findByBabyAndDeletedAtIsNull(baby);

        List<User> userList = custodyList.stream().map(Custody::getUser).toList();

        List<FcmToken> fcmTokenList = userList.stream()
                .map(fcmTokenRepository::findByUser)
                .flatMap(List::stream)
                .toList();

        for (FcmToken fcmToken : fcmTokenList) {
            sendNotification(fcmToken.getFcmToken(), title, body);
        }

    }

    /**
     * 특정 FCM 토큰에 알림을 전송합니다.
     *
     * <p>전송 중 `UNREGISTERED` 오류가 발생하면 해당 토큰을 삭제합니다.</p>
     *
     * @param token FCM 토큰
     * @param title 알림 제목
     * @param body  알림 내용
     */
    @Transactional
    public void sendNotification(String token, String title, String body) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .setToken(token)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            if (MessagingErrorCode.UNREGISTERED.equals(e.getMessagingErrorCode())) {
                fcmTokenRepository.findByFcmToken(token).ifPresent(fcm -> fcmTokenRepository.deleteByFcmToken(token));
            }
        }
    }
}
