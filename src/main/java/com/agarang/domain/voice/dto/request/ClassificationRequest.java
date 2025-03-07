package com.agarang.domain.voice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.speechRecognition.dto<br>
 * fileName       : ClassificationRequest.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 13.<br>
 * description    : 음성 인식된 텍스트를 분류하는 요청 DTO 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.13          okeio           최초생성<br>
 * 25.02.19          okeio           thread id 제거<br>
 * <br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "텍스트 분류 요청 DTO")
public class ClassificationRequest {
    @Schema(description = "분류할 텍스트", example = "3시에 기저귀를 갈았어.")
    @NotBlank
    private String text;

    @Schema(description = "아기 ID", example = "1")
    @NotBlank
    @Min(1)
    @JsonProperty("baby_id")
    private Integer babyId;
}
