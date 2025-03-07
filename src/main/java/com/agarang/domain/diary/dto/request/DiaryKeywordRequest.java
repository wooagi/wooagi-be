package com.agarang.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * packageName    : com.agarang.domain.diary.dto.request<br>
 * fileName       : DiaryKeywordRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 15.<br>
 * description    :  DiaryKeyword entity 의 Request dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.15          Fiat_lux           최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "다이어리 키워드 수정 요청 객체")
public class DiaryKeywordRequest {

    @Schema(description = "키워드 ID", example = "10")
    @NotNull
    @Min(1)
    private Integer id;

    @Schema(description = "새로운 키워드 내용", example = "행복")
    @NotNull
    @NotBlank
    @Length(min = 1, max = 10)
    private String name;
}
