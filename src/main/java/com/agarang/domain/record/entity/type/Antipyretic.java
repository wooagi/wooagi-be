package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.enumeration.AntipyreticType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Antipyretic.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 해열제 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "antipyretic")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Antipyretic {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Medication medication;

    @Column(name = "specific_type", nullable = false, length = 13)
    @Enumerated(EnumType.STRING)
    private AntipyreticType specificType;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal amount;
}
