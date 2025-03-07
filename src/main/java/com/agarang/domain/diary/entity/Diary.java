package com.agarang.domain.diary.entity;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.diary.entity<br>
 * fileName       : Diary.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  diary 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux             최초생성<br>
 */
@Entity
@Table(name = "diary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @Column(name = "diary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer diaryId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, length = 255)
    private String emoji;

    @Column(name = "written_date", nullable = false)
    private LocalDate writtenDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "baby_id", nullable = false)
    private Baby baby;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Diary(String content, String emoji, LocalDate writtenDate, Baby baby, User user) {
        this.content = content;
        this.emoji = emoji;
        this.writtenDate = writtenDate;
        this.baby = baby;
        this.user = user;
    }
}
