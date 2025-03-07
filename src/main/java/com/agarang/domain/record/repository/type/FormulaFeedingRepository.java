package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.type.FormulaFeeding;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : FormulaFeedingRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 분유수유 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface FormulaFeedingRepository extends JpaRepository<FormulaFeeding, Integer> {

    @Query("SELECT COALESCE(AVG(ff.amount), 0) " +
            "FROM FormulaFeeding ff JOIN Record r " +
            "ON ff.recordId = r.recordId " +
            "WHERE r.recordType = 'FEEDING' " +
            "AND r.baby = :baby " +
            "AND DATE(r.startedAt) = :date")
    Double findAverageByBabyAndDate(@Param("baby") Baby baby,
                                    @Param("date") LocalDate date);

    @Query("SELECT ff " +
            "FROM FormulaFeeding ff " +
            "JOIN Record r " +
            "ON ff.recordId = r.recordId " +
            "WHERE r.baby = :baby " +
            "AND DATE(r.startedAt) BETWEEN :start AND :end " +
            "ORDER By r.startedAt DESC " +
            "LIMIT 1")
    Optional<FormulaFeeding> getLatestFormulaFeedingByBabyAndStartedAtBetween(@Param("baby") Baby baby,
                                                                             @Param("start") LocalDate start,
                                                                             @Param("end") LocalDate end);
}
