package com.agarang.domain.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.diary.dto.response<br>
 * fileName       : DiaryResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    : Diary Response dto 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24         Fiat_lux            최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "다이어리 등록 응답 객체")
public class DiaryResponse {

    @Schema(description = "등록된 다이어리 ID", example = "101")
    @JsonProperty("diary_id")
    private Integer diaryId;

    @Schema(description = "다이어리 내용", example = "오늘은 정말 행복한 하루였어요!")
    private String content;

    @Schema(description = "이모지", example = "https://qwerqwer.com/qwewqwe")
    private String emoji;

    @Schema(description = "다이어리 작성 날짜", example = "2025-02-19")
    @JsonProperty("written_date")
    private LocalDate writtenDate;
}