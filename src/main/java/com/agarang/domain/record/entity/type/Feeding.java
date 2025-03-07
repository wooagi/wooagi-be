package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.FeedingType;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Feeding.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 수유 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "feeding")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Feeding {

    @Id
    private Integer recordId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "record_id")
    private Record record;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FeedingType feedingType = FeedingType.NORMAL_FEEDING;
}
