package com.agarang.domain.alarm.entity;

import com.agarang.domain.baby.entity.Baby;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.alarm.entity<br>
 * fileName       : NotificationJob.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  알림 작업 entity class 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.06          Fiat_lux           최초생성<br>
 */
@Entity
@Table(name = "notification_job")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationJob {
    @Id
    @Column(name = "notification_job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationJobId;

    @Column
    private String title;

    @Column
    private String body;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "flag_send")
    private Boolean flagSend;

    @JoinColumn(name = "baby_id")
    @ManyToOne
    private Baby baby;


    public NotificationJob(String title, String body, LocalDateTime scheduledAt, Baby baby) {
        this.title = title;
        this.body = body;
        this.scheduledAt = scheduledAt;
        this.flagSend = false;
        this.baby = baby;
    }
}
