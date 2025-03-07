package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.SleepHour;
import com.agarang.domain.record.entity.type.Sleep;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : SleepRepository.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-23<br>
 * description    : 수면 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-23          nature1216          최초생성<br>
 * <br>
 */
public interface SleepRepository extends JpaRepository<Sleep, Integer> {

    @Query("SELECT new com.agarang.domain.record.dto.SleepHour(r.startedAt, s.endedAt) " +
            "FROM Record r JOIN Sleep s " +
            "ON r.recordId = s.recordId " +
            "WHERE r.baby = :baby " +
            "AND r.startedAt BETWEEN :start AND :end")
    List<SleepHour> findSleepByBabyAndDateRange(@Param("baby") Baby baby,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);
}
