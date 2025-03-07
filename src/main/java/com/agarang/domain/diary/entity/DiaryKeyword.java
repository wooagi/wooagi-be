package com.agarang.domain.diary.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * packageName    : com.agarang.domain.diary.entity<br>
 * fileName       : DiaryKeyword.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  diary_keyword 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux             최초생성<br>
 * <br>
 */
@Entity
@Table(name = "diary_keyword")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DiaryKeyword {

    @Id
    @Column(name = "keyword_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer keywordId;

    @Column(nullable = false, length = 10)
    private String name;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    public DiaryKeyword(String name, Diary diary) {
        this.name = name;
        this.diary = diary;
    }
}
