package com.agarang.domain.growthStandard.repository;

import com.agarang.domain.growthStandard.entity.GrowthStandard;
import com.agarang.domain.growthStandard.entity.GrowthStandardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.agarang.domain.growthStandard.repository<br>
 * fileName       : GrowthStandardRepository.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장 발달 Repository입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          okeio           최초생성<br>
 * <br>
 */
@Repository
public interface GrowthStandardRepository extends JpaRepository<GrowthStandard, Integer> {
    List<GrowthStandard> findByStartDayLessThanEqualAndEndDayGreaterThanEqualAndGrowthStandardType(int startDay, int endDay, GrowthStandardType growthStandardType);

    @Query("SELECT g FROM GrowthStandard g WHERE g.startDay <= :day AND g.endDay >= :day AND g.growthStandardType IN :growthStandardTypes")
    List<GrowthStandard> findByDayAndGrowthStandardTypes(@Param("day") int day, @Param("growthStandardTypes") List<String> growthStandardTypes);
}
