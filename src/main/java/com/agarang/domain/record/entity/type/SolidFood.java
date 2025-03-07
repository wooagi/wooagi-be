package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : SolidFood.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 이유식 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "solid_food")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class SolidFood {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(nullable = false)
    @Min(0) @Max(32767)
    private Integer amount;
}
