package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.FeverPosition;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Fever.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 발열 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "fever")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Fever {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private FeverPosition position;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal temperature;
}
