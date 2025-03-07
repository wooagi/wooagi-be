package com.agarang.domain.record.repository;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.LatestStartedAt;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.record.repository<br>
 * fileName       : RecordRepository.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-22<br>
 * description    : 기록 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-22          nature1216          최초생성<br>
 * <br>
 */

public interface RecordRepository extends JpaRepository<Record, Integer> {

    @Query("SELECT r.recordType FROM Record r WHERE r.recordId = :recordId")
    Optional<RecordType> findRecordTypeById(@Param("recordId") Integer recordId);

    List<Record> findAllByRecordTypeAndBabyAndStartedAtBetweenOrderByStartedAtDesc
            (RecordType recordType, Baby baby, LocalDateTime start, LocalDateTime end);

    List<Record> findAllByBabyAndStartedAtBetweenAndRecordTypeNotInOrderByStartedAtDesc
            (Baby baby, LocalDateTime start, LocalDateTime end, List<RecordType> recordTypes);

    @Query("SELECT COUNT(*) FROM Record r WHERE r.baby = :baby AND r.recordType = :recordType AND r.startedAt BETWEEN :start AND :end")
    Integer getCountByRecordTypeAndBaby(@Param("baby") Baby baby,
                                        @Param("recordType") RecordType recordType,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    @Query("SELECT r " +
            "FROM Record r " +
            "WHERE r.recordType = :recordType " +
            "AND r.startedAt BETWEEN :start AND :end " +
            "AND r.baby = :baby")
    List<Record> findStartedAtByBabyAndDateBetween(@Param("baby") Baby baby,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end,
                                                   @Param("recordType") RecordType recordType);

    @Query("SELECT new com.agarang.domain.record.dto.LatestStartedAt (MAX(r.startedAt), r.recordType) " +
            "FROM Record r " +
            "WHERE r.startedAt BETWEEN :start AND :end " +
            "AND r.baby = :baby " +
            "GROUP BY r.recordType")
    List<LatestStartedAt> getLatestStartedAtByBabyAndDateBetweenAndRecordType(@Param("baby") Baby baby,
                                                                              @Param("start") LocalDateTime start,
                                                                              @Param("end") LocalDateTime end);

    Optional<Record> findTopByRecordTypeAndBabyAndStartedAtBetweenOrderByStartedAtDesc(RecordType recordType,
                                                                                       Baby baby,
                                                                                       LocalDateTime start,
                                                                                       LocalDateTime end);
}
