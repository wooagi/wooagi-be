package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.HospitalVisitType;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Hospital.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 병원 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "hospital")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Hospital {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(name = "visit_type", length = 11, nullable = false)
    @Enumerated(EnumType.STRING)
    private HospitalVisitType visitType;
}
