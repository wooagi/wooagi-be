package com.agarang.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * packageName    : com.agarang.domain.diary.dto.request<br>
 * fileName       : DiaryKeywordRegisterRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 15.<br>
 * description    :  DiaryKeyword entity 의 Register Request dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.15          Fiat_lux           최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "다이어리 키워드 등록 요청 객체")
public class DiaryKeywordRegisterRequest {

    @Schema(description = "새로 추가할 키워드", example = "즐거움")
    @NotNull
    @NotBlank
    @Length(min = 1, max = 10)
    private String name;
}