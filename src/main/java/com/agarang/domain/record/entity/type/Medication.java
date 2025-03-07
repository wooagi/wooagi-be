package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.MedicationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Medication.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 약 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "medication")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Medication {
    @Id
    private Integer recordId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(name = "medication_type", length = 11, nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicationType medicationType;

}
