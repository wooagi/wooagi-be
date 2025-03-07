package com.agarang.domain.record.entity.type;

import com.agarang.domain.record.entity.Record;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.entity.type<br>
 * fileName       : Sleep.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 수면 entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Entity
@Table(name = "sleep")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
@Builder
public class Sleep {
    @Id
    @Column(name = "record_id")
    private Integer recordId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "record_id")
    private Record record;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @PrePersist
    public void setDefaultEndedAt() {
        if(endedAt == null && record != null) {
            this.endedAt = record.getStartedAt();
        }
    }
}
