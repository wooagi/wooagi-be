package com.agarang.domain.diary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * packageName    : com.agarang.domain.diary.dto.request<br>
 * fileName       : DiaryUpdateRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  Diary entity 의 update request dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 * 25.02.13          Fiat_lux           validation 적용<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "다이어리 수정 요청 객체")
public class DiaryUpdateRequest {

    @Schema(description = "다이어리 내용", example = "오늘은 정말 행복한 하루였어요!")
    @NotBlank
    private String content;

    @Schema(description = "이모지 (255자 제한)", example = "https://qwer.qwer/qwer")
    @NotBlank
    @Length(min = 1, max = 255)
    private String emoji;

    @Schema(description = "수정할 키워드 목록")
    @JsonProperty("update_keyword")
    private List<DiaryKeywordRequest> updateKeyword;

    @Schema(description = "삭제할 키워드 ID 목록")
    @JsonProperty("delete_keyword")
    private List<Integer> deleteKeyword;

    @Schema(description = "새로 추가할 키워드 목록")
    @JsonProperty("new_keyword")
    private List<DiaryKeywordRegisterRequest> newKeyword;
}
