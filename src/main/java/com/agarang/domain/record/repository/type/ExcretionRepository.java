package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.entity.type.Excretion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : ExcretionRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/27/25<br>
 * description    : 배변 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/27/25          nature1216          최초생성<br>
 */
public interface ExcretionRepository extends JpaRepository<Excretion, Integer> {

    @Query("SELECT COUNT(*) FROM Excretion e " +
            "JOIN Record r " +
            "ON r.recordId = e.recordId " +
            "WHERE r.startedAt BETWEEN :start AND :end " +
            "AND r.baby = :baby " +
            "AND e.excretionType != 'PEE' " +
            "AND e.excretionStatus != 'GOOD'")
    Integer countAbnormalExcretionByDateAndBaby(@Param("baby")Baby baby,
                                                @Param("start")LocalDateTime start,
                                                @Param("end")LocalDateTime end);

    @Query("SELECT e.excretionStatus, COUNT(e) " +
            "FROM Excretion e JOIN Record r " +
            "ON e.recordId = r.recordId " +
            "WHERE r.baby = :baby " +
            "AND e.excretionType != 'PEE'" +
            "AND r.startedAt BETWEEN :start AND :end " +
            "GROUP BY e.excretionStatus")
    List<Object[]> countExcretionStatusByBabyDateBetween(@Param("baby")Baby baby,
                                                         @Param("start")LocalDateTime start,
                                                         @Param("end")LocalDateTime end);

    @Query("SELECT e.color, COUNT(e) " +
            "FROM Excretion e JOIN Record r " +
            "ON e.recordId = r.recordId " +
            "WHERE r.baby = :baby " +
            "AND e.excretionType != 'PEE'" +
            "AND r.startedAt BETWEEN :start AND :end " +
            "GROUP BY e.color")
    List<Object[]> countExcretionColorByBabyDateBetween(@Param("baby")Baby baby,
                                                        @Param("start")LocalDateTime start,
                                                        @Param("end")LocalDateTime end);
}
