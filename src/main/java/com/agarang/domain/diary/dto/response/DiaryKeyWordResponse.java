package com.agarang.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.diary.dto.response<br>
 * fileName       : DiaryKeyWordResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  DiaryKeyword entity 의 response dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "다이어리 키워드 응답 객체")
public class DiaryKeyWordResponse {

    @Schema(description = "키워드 ID", example = "10")
    private Integer id;

    @Schema(description = "키워드 내용", example = "행복")
    private String name;
}