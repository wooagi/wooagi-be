package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.type.Pumping;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : PumpingRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 유축 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface PumpingRepository extends JpaRepository<Pumping, Integer> {
    @Query("SELECT p " +
            "FROM Pumping p " +
            "JOIN Record r " +
            "ON p.record = r " +
            "WHERE r.baby = :baby " +
            "AND DATE(r.startedAt) BETWEEN :start AND :end " +
            "ORDER BY r.startedAt DESC " +
            "LIMIT 1")
    Optional<Pumping> getLatestPumpingByBabyAndStartedAtBetween(@Param("baby") Baby baby,
                                                                @Param("start") LocalDate start,
                                                                @Param("end") LocalDate end);
}
