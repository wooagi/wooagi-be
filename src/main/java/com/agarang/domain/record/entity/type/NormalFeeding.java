package com.agarang.domain.record.entity.type;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : NormalFeeding.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 일반수유 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */

@Entity
@Table(name = "normal_feeding")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class NormalFeeding {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Feeding feeding;

    @Column(nullable = false)
    @Min(0) @Max(32767)
    @Builder.Default
    private Integer amount = 0;
}
