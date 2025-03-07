package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.type.SolidFood;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : SolidFoodRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface SolidFoodRepository extends JpaRepository<SolidFood, Integer> {
    @Query("SELECT SUM(sf.amount)" +
            "FROM SolidFood sf JOIN Record r " +
            "ON sf.recordId = r.recordId " +
            "AND r.baby = :baby " +
            "AND r.startedAt BETWEEN :start AND :end")
    Integer getSumAmountByBabyAndDate(@Param("baby") Baby baby,
                                      @Param("start") LocalDateTime start,
                                      @Param("end")LocalDateTime end);
}
