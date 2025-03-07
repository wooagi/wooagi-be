package com.agarang.domain.statistics.repository;

import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.agarang.domain.statistics.dto.response.GrowthAverageResponse;
import com.agarang.domain.statistics.entity.GrowthPercentile;
import com.agarang.domain.user.entity.Sex;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.statistics.repository<br>
 * fileName       : GrowthPercentileRepository.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : GrowthPercentile entity의 repository입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */
public interface GrowthPercentileRepository extends JpaRepository<GrowthPercentile, Integer> {
    @Query(value = "SELECT new com.agarang.domain.statistics.dto.response.GrowthAverageResponse" +
            "(gp.day, gp.p50) " +
            "FROM GrowthPercentile gp " +
            "WHERE gp.day <= :day " +
            "AND MOD(gp.day, 14) = 0 " +
            "AND gp.sex = :sex " +
            "AND gp.type = :type " +
            "ORDER BY gp.day")
    List<GrowthAverageResponse> findGrowthAverageByDayAndSexAndType(@Param("day") Integer day,
                                                                    @Param("sex") Sex sex,
                                                                    @Param("type") GrowthStatusType type);

    Optional<GrowthPercentile> findByDayAndSexAndType(Integer day, Sex sex, GrowthStatusType type);
}
