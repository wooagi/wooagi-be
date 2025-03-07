package com.agarang.domain.record.entity;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.entity<br>
 * fileName       : Record.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 기록 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Data
@Table(name = "record")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Record {
    @Id
    @Column(name = "record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baby_id", nullable = false)
    private Baby baby;

    @Column(name = "record_image")
    private String recordImage;

    @Column
    private String content;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "record_type", nullable = false, updatable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private RecordType recordType;
}
