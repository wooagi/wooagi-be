package com.agarang.domain.alarm.repository;

import com.agarang.domain.alarm.entity.FcmToken;
import com.agarang.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.alarm.repository<br>
 * fileName       : FcmTokenRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 5.<br>
 * description    :  fcm token entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.05           Fiat_lux         최초생성<br>
 * <br>
 */
@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Integer> {

    /**
     * 특정 사용자와 FCM 토큰에 해당하는 엔티티를 조회합니다.
     *
     * @param user     FCM 토큰을 소유한 사용자
     * @param fcmToken 검색할 FCM 토큰
     * @return {@link Optional} FCM 토큰 엔티티 (존재하지 않을 경우 빈 값 반환)
     */
    Optional<FcmToken> findByUserAndFcmToken(User user, String fcmToken);

    /**
     * 특정 사용자의 모든 FCM 토큰을 조회합니다.
     *
     * @param user FCM 토큰을 소유한 사용자
     * @return 사용자의 FCM 토큰 리스트
     */
    List<FcmToken> findByUser(User user);

    /**
     * 특정 날짜 이전에 업데이트된 FCM 토큰을 조회합니다.
     *
     * @param thresholdDate 비교 기준이 되는 날짜
     * @return 특정 날짜 이전에 업데이트된 FCM 토큰 리스트
     */
    List<FcmToken> findByUpdatedAtBefore(LocalDate thresholdDate);

    /**
     * 지정된 FCM 토큰 리스트에 포함된 모든 FCM 토큰을 삭제합니다.
     *
     * @param tokens 삭제할 FCM 토큰 리스트
     */
    void deleteByFcmTokenIn(List<String> tokens);

    /**
     * 특정 FCM 토큰에 해당하는 엔티티를 조회합니다.
     *
     * @param fcmToken 검색할 FCM 토큰
     * @return {@link Optional} FCM 토큰 엔티티 (존재하지 않을 경우 빈 값 반환)
     */
    Optional<FcmToken> findByFcmToken(String fcmToken);

    /**
     * 특정 FCM 토큰을 삭제합니다.
     *
     * @param token 삭제할 FCM 토큰
     */
    void deleteByFcmToken(String token);
}
