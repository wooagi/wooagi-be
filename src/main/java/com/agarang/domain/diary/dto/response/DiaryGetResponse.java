package com.agarang.domain.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.agarang.domain.diary.service<br>
 * fileName       : DiaryGetResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  Diary 조회 response dto 클래스<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 * 25.02.13          Fiat_lux           snake case 적용<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "다이어리 상세 조회 응답 객체")
public class DiaryGetResponse {

    @Schema(description = "다이어리 ID", example = "101")
    @JsonProperty("diary_id")
    private Integer diaryId;

    @Schema(description = "다이어리 내용", example = "오늘은 정말 행복한 하루였어요!")
    private String content;

    @Schema(description = "다이어리 이모지", example = "https://qwerqwer.qwerqw/qwer")
    private String emoji;

    @Schema(description = "다이어리 작성 날짜", example = "2025-02-19")
    @JsonProperty("written_date")
    private LocalDate writtenDate;

    @Schema(description = "아기 출생 이후 경과 일수", example = "365")
    @JsonProperty("birth_day")
    private Long birthDay;

    @Schema(description = "다이어리에 연관된 키워드 목록")
    @JsonProperty("diary_key_word_response_list")
    private List<DiaryKeyWordResponse> diaryKeyWordResponseList;
}
