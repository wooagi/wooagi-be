package com.agarang.domain.diary.repository;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.diary.repository<br>
 * fileName       : DiaryRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  일기 entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24           Fiat_lux          최초 생성<br>
 */
@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findByBabyAndWrittenDateBetween(Baby baby, LocalDate startDate, LocalDate endDate);

    Optional<Diary> findByBabyAndWrittenDate(Baby baby, LocalDate date);

    @Query("SELECT DISTINCT d FROM Diary d " +
            "LEFT JOIN DiaryKeyword dk ON d.diaryId = dk.diary.diaryId " +
            "WHERE d.baby.babyId = :babyId " +
            "AND d.writtenDate BETWEEN :startDate AND :endDate " +
            "AND (:search IS NULL OR dk.name = :search)" +
            "ORDER BY d.writtenDate ASC")
    List<Diary> findDiariesByMonthAndSearch(@Param("babyId") Integer babyId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("search") String search);

    boolean existsByBabyAndWrittenDate(Baby baby, LocalDate date);
}
