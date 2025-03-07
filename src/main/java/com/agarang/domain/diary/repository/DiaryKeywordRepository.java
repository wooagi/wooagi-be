package com.agarang.domain.diary.repository;

import com.agarang.domain.diary.entity.Diary;
import com.agarang.domain.diary.entity.DiaryKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.agarang.domain.diary.repository<br>
 * fileName       : DiaryKeywordRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  Diary key word entity repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 */
public interface DiaryKeywordRepository extends JpaRepository<DiaryKeyword, Integer> {
    List<DiaryKeyword> findByDiary(Diary diary);

    void deleteByDiary(Diary diary);
}
