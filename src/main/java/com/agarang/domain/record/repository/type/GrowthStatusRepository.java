package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.agarang.domain.record.entity.type.GrowthStatus;
import com.agarang.domain.statistics.dto.GrowthRecord;
import com.agarang.domain.statistics.dto.response.GrowthResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : GrowthStatusRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 발육상태 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface GrowthStatusRepository extends JpaRepository<GrowthStatus, Integer> {
    @Query("SELECT new com.agarang.domain.statistics.dto.response.GrowthResponse" +
            "(r.recordId, gs.size, r.startedAt, r.baby.birth) " +
            "FROM Record r " +
            "JOIN " +
            "(SELECT DATE(r2.startedAt) AS record_date, MAX(r2.startedAt) AS latest " +
            "FROM Record r2 " +
            "JOIN GrowthStatus gs ON r2 = gs.record " +
            "WHERE gs.growthStatusType = :type " +
            "AND r2.baby = :baby " +
            "GROUP BY DATE(r2.startedAt)) latest_records " +
            "ON DATE(r.startedAt) = latest_records.record_date " +
            "JOIN GrowthStatus gs " +
            "ON gs.record = r " +
            "AND r.startedAt = latest_records.latest " +
            "WHERE r.baby = :baby " +
            "AND gs.growthStatusType = :type"
            )
    List<GrowthResponse> findAllByGrowthStatusTypeAndBaby(@Param("type") GrowthStatusType type, @Param("baby") Baby baby);

    @Query("SELECT new com.agarang.domain.statistics.dto.GrowthRecord(r.recordId, gs.growthStatusType, gs.size, r.startedAt) " +
            "FROM GrowthStatus gs " +
            "JOIN Record r ON r = gs.record " +
            "WHERE DATE(r.startedAt) <= :targetDate " +
            "AND gs.growthStatusType = :type " +
            "ORDER BY r.startedAt DESC " +
            "LIMIT 1")
    Optional<GrowthRecord> findTopByChildIdAndStartedAtBeforeOrderByStartedAtDesc(@Param("baby") Baby baby,
                                                                                  @Param("targetDate") LocalDate targetDate,
                                                                                  @Param("type") GrowthStatusType type);

    @Query("SELECT gs.size " +
            "FROM GrowthStatus gs " +
            "JOIN Record r ON gs.recordId = r.recordId " +
            "WHERE r.baby = :baby " +
            "AND gs.growthStatusType = 'WEIGHT' " +
            "ORDER BY r.startedAt DESC\n" +
            "limit 1")
    Optional<BigDecimal> findRecentWeightByBaby(@Param("baby") Baby baby);
}
