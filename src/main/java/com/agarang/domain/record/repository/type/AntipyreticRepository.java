package com.agarang.domain.record.repository.type;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.AntipyreticInfo;
import com.agarang.domain.record.entity.type.Antipyretic;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
/**
 * packageName    : com.agarang.domain.record.repository.type<br>
 * fileName       : AntipyreticRepository.java<br>
 * author         : nature1216 <br>
 * date           : 1/30/25<br>
 * description    : 해열제 entity의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/30/25          nature1216          최초생성<br>
 */
public interface AntipyreticRepository extends JpaRepository<Antipyretic, Integer> {

    @Query("SELECT new com.agarang.domain.record.dto.AntipyreticInfo(r.recordId, r.baby.babyId, a.specificType, a.amount, r.startedAt ) " +
            "FROM Antipyretic a " +
            "JOIN Record r ON r.recordId = a.recordId " +
            "WHERE r.baby = :baby " +
            "AND r.recordId != :excludedRecordId " +
            "AND r.startedAt BETWEEN :start AND :end")
    List<AntipyreticInfo> findAntipyreticByBabyAndStartedAt(@Param("baby")Baby baby,
                                                            @Param("excludedRecordId") Integer excludedRecordId,
                                                            @Param("start")LocalDateTime start,
                                                            @Param("end") LocalDateTime end);
}
