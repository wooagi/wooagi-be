package com.agarang.domain.growthStandard.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.agarang.domain.growthStandard.entity<br>
 * fileName       : GrowthStandard.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장발달 Entity 입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24           okeio           최초생성<br>
 * <br>
 */
@Entity
@Table(name = "baby_growth_standard")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthStandard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "start_day")
    private Integer startDay;

    @Column(nullable = false, name = "end_day")
    private Integer endDay;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 9, name = "type")
    @Enumerated(EnumType.STRING)
    private GrowthStandardType growthStandardType;

}
