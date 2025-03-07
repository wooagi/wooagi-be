package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : GrowthStatus.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 발육상태 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "growth_status")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class GrowthStatus {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(name = "growth_status_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GrowthStatusType growthStatusType;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal size;
}
