package com.agarang.domain.custody.entity;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.custody.entity<br>
 * fileName       : Custody.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Custody entity 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22         Fiat_lux            최초생성<br>
 */
@Entity
@Table(name = "custody")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Custody {

    @Id
    @Column(name = "custody_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer custodyId;

    @JoinColumn(name = "baby_id", nullable = false)
    @ManyToOne
    private Baby baby;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(name = "custody_type", nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private CustodyType custodyType;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public Custody(Baby baby, User user, CustodyType custodyType) {
        this.baby = baby;
        this.user = user;
        this.custodyType = custodyType;
    }
}
