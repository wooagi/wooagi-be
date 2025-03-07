package com.agarang.domain.statistics.entity;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.agarang.domain.user.entity.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName    : com.agarang.domain.statistics.entity<br>
 * fileName       : GrowthPercentile.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-03<br>
 * description    : GrowthPercentile entity입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-03          nature1216          최초생성<br>
 * <br>
 */
@Entity
@Table(name = "growth_percentile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthPercentile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private GrowthStatusType type;

    @Column(nullable = false)
    private Integer day;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p3;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p10;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p25;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p50;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p75;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p90;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p97;

    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal p99;

    public Map<Integer, BigDecimal> getPercentileMap() {
        Map<Integer, BigDecimal> percentileMap = new HashMap<>();
        percentileMap.put(3, this.p3);
        percentileMap.put(10, this.p10);
        percentileMap.put(25, this.p25);
        percentileMap.put(50, this.p50);
        percentileMap.put(75, this.p75);
        percentileMap.put(90, this.p90);
        percentileMap.put(97, this.p97);
        percentileMap.put(100, this.p99);

        return percentileMap;
    }
}
