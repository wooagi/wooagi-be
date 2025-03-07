package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.PumpingPosition;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Pumping.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 유축 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "pumping")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
@ToString
public class Pumping {

    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PumpingPosition position;

    @Column(name = "right_amount", nullable = false)
    @Min(0) @Max(32767)
    private Integer rightAmount;

    @Column(name = "left_amount", nullable = false)
    @Min(0) @Max(32767)
    private Integer leftAmount;

    @Column(name = "total_amount", nullable = false)
    @Min(0) @Max(32767)
    private Integer totalAmount;
}
