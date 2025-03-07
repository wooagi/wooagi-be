package com.agarang.domain.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.diary.service<br>
 * fileName       : DiaryListResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  Diary entity 의 list 조회 response dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "다이어리 목록 응답 객체")
public class DiaryListResponse {

    @Schema(description = "다이어리 ID", example = "101")
    @JsonProperty("diary_id")
    private Integer diaryId;

    @Schema(description = "다이어리 이모지", example = "https://qwerqwer.qwerqwer/qwerqwer")
    private String emoji;

    @Schema(description = "다이어리 작성 날짜", example = "2025-02-19")
    @JsonProperty("written_date")
    private LocalDate writtenDate;

    @Schema(description = "다이어리에 연관된 키워드 목록")
    @JsonProperty("keyword_response_list")
    private List<DiaryKeyWordResponse> keyWordResponseList;
}