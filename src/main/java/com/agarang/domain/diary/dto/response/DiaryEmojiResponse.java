package com.agarang.domain.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.diary.dto.response<br>
 * fileName       : DiaryEmojiResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 13.<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13         Fiat_lux            최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "다이어리 이모지 응답 객체")
public class DiaryEmojiResponse {

    @Schema(description = "이모지", example = "https://qwerqwer.qwerqwer/qwerqwer")
    private String emoji;

    @Schema(description = "다이어리 작성 날짜", example = "2025-02-19")
    @JsonProperty("written_date")
    private LocalDate writtenDate;
}