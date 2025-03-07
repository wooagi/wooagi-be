package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.dto.request.ExcretionCreateRequest;
import com.agarang.domain.record.entity.enumeration.ExcretionColor;
import com.agarang.domain.record.entity.enumeration.ExcretionStatus;
import com.agarang.domain.record.entity.enumeration.ExcretionType;
import com.agarang.domain.record.entity.Record;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Excretion.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 배변 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "excretion")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Excretion {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(
            name = "excretion_type",
            nullable = false,
            length = 4
    )
    @Enumerated(EnumType.STRING)
    private ExcretionType excretionType;

    @Column(length = 11)
    @Enumerated(EnumType.STRING)
    private ExcretionColor color;

    @Column(name = "excretion_status", length = 8)
    @Enumerated(EnumType.STRING)
    private ExcretionStatus excretionStatus;

    public static Excretion create(Record record, ExcretionCreateRequest request) {
        if(request.getExcretionType() == ExcretionType.PEE) {
            return Excretion.builder()
                    .record(record)
                    .excretionType(ExcretionType.PEE)
                    .excretionStatus(null)
                    .color(null)
                    .build();
        } else {
            return Excretion.builder()
                    .record(record)
                    .excretionType(request.getExcretionType())
                    .excretionStatus(request.getExcretionStatus() == null ? ExcretionStatus.GOOD : request.getExcretionStatus())
                    .color(request.getColor() == null ? ExcretionColor.LIGHT_BROWN : request.getColor())
                    .build();
        }
    }

}
