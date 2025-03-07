package com.agarang.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.*;
import com.agarang.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.alarm.entity<br>
 * fileName       : FcmToken.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 5.<br>
 * description    :  Fcm token entity 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.05           Fiat_lux          최초생성<br>
 */
@Entity
@Table(name = "fcm_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class FcmToken {

    @Id
    @Column(name = "fcm_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fcmTokenId;

    @Column(name = "fcm_token", nullable = false, length = 255)
    private String fcmToken;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FcmToken(String fcmToken, User user) {
        this.fcmToken = fcmToken;
        this.user = user;
        this.updatedAt = LocalDate.now();
    }
}
