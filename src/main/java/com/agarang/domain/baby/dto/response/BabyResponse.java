package com.agarang.domain.baby.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.baby.dto.response<br>
 * fileName       : BabyResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Baby entity response dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "아기 등록 응답 객체")
public class BabyResponse {

    @Schema(description = "아기의 ID", example = "1")
    @JsonProperty("baby_id")
    private Integer babyId;
}
