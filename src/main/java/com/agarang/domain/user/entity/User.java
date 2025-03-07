package com.agarang.domain.user.entity;

import com.agarang.global.s3.S3DefaultImage;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.user.entity<br>
 * fileName       : User.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 회원 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birth;

    @Column(nullable = false, length = 100)
    @Email
    private String email;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "user_image", nullable = false)
    private String userImage;

    @Column(name = "provider_id", unique = true)
    private String providerId;

    public boolean isDeactivated() {
        return deletedAt != null && deletedAt.isAfter(LocalDateTime.now().minusYears(1));
    }

    public boolean isAnonymized() {
        return deletedAt != null && deletedAt.isBefore(LocalDateTime.now().minusYears(1));
    }

    public void anonymize() {
        this.name = "(알수없음)";
        this.email = "unknown@agarang.com";
        this.birth = LocalDate.of(1900, 01, 01);
        this.sex = Sex.MALE;
        this.userImage = S3DefaultImage.USER_MALE_DEFAULT_IMAGE;
        this.providerId = null;
    }
}
