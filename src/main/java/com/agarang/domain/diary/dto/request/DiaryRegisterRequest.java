package com.agarang.domain.diary.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.diary.dto.request<br>
 * fileName       : DiaryRegisterRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 30.<br>
 * description    :  Diary entity 의 register request dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.30          Fiat_lux           최초생성<br>
 * 25.02.13          Fiat_lux           validation 적용<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "다이어리 등록 요청 객체")
public class DiaryRegisterRequest {

    @Schema(description = "다이어리 내용", example = "오늘은 정말 행복한 하루였어요!")
    @NotBlank
    private String content;

    @Schema(description = "이모지 (255자 제한)", example = "😊")
    @NotBlank
    @Size(min = 1, max = 255)
    private String emoji;

    @Schema(description = "다이어리 작성 날짜 (yyyy-MM-dd 형식)", example = "2025-02-19")
    @NotNull
    @JsonProperty("written_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate writtenDate;
}
