package com.agarang.domain.baby.entity;

import com.agarang.domain.user.entity.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.baby.entity<br>
 * fileName       : Baby.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Baby entity 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 */
@Entity
@Table(name = "baby")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Baby {
    @Id
    @Column(name = "baby_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer babyId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private LocalDateTime birth;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(nullable = false, length = 255, name = "baby_image")
    private String babyImage;

    @Column(precision = 3, scale = 1)
    private BigDecimal weight;

    @Column(precision = 4, scale = 1)
    private BigDecimal height;

    @Column(precision = 4, scale = 2, name = "head_size")
    private BigDecimal headSize;

    @Column(nullable = false, name = "flag_deleted")
    private Boolean flagDeleted;

    @Column(nullable = false, name = "flag_diary_permission")
    private Boolean flagDiaryPermission;

    public Baby(String name, LocalDateTime birth, Sex sex, String babyImage) {
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.babyImage = babyImage;
        this.flagDeleted = false;
        this.flagDiaryPermission = false;
    }

}
