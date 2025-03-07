package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.enumeration.BreastFeedingPosition;
import com.agarang.domain.record.entity.Record;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : BreastFeeding.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 모유수유 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "breast_feeding")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class BreastFeeding {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Feeding feeding;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BreastFeedingPosition position = BreastFeedingPosition.UNKNOWN;

    @Column(name = "left_time", nullable = false)
    @Min(0) @Max(127)
    @Builder.Default
    private Integer leftTime = 0;

    @Column(name = "right_time", nullable = false)
    @Min(0) @Max(127)
    @Builder.Default
    private Integer rightTime = 0;

    @Column(name = "total_time", nullable = false)
    @Min(0) @Max(127)
    @Builder.Default
    private Integer totalTime = 0;

}
