package com.agarang.domain.diary.dto.mapper;

import com.agarang.domain.diary.dto.response.DiaryKeyWordResponse;
import com.agarang.domain.diary.entity.DiaryKeyword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.agarang.domain.diary.dto.mapper<br>
 * fileName       : DiaryKeywordMapper.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  DiaryKeyword entity 의 map struct 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DiaryKeywordMapper {

    @Mapping(source = "keywordId", target = "id")
    DiaryKeyWordResponse mapToResponse(DiaryKeyword diaryKeyword);

    List<DiaryKeyWordResponse> mapToResponseList(List<DiaryKeyword> diaryKeywords);
}
