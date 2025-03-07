package com.agarang.domain.diary.dto.mapper;

import com.agarang.domain.diary.dto.response.DiaryEmojiResponse;
import com.agarang.domain.diary.dto.response.DiaryResponse;
import com.agarang.domain.diary.entity.Diary;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.agarang.domain.diary.dto.mapper<br>
 * fileName       : DiaryMapper.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  Diary entity 의 Map struct 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux           최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DiaryMapper {
    DiaryEmojiResponse mapToDiaryEmojiResponse(Diary diary);

    List<DiaryEmojiResponse> mapToDiaryEmojiResponseList(List<Diary> diaryList);

    DiaryResponse mapToDiaryResponse(Diary diary);
}
