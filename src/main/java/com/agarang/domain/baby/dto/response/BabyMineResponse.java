package com.agarang.domain.baby.dto.response;

import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * packageName    : com.agarang.domain.baby.dto.response<br>
 * fileName       : BabyMineResponse.java<br>
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
@Schema(description = "내가 관리하는 아기 목록 응답 객체")
public class BabyMineResponse {

    @JsonProperty("baby_id")
    @Schema(description = "아기의 ID", example = "1")
    private Integer babyId;

    @Schema(description = "아기의 이름", example = "김아기")
    private String name;

    @JsonProperty("baby_image")
    @Schema(description = "아기의 프로필 이미지 URL", example = "https://s3.amazonaws.com/my-bucket/baby.jpg")
    private String babyImage;

    @JsonProperty("custody_type")
    @Schema(description = "보호자 타입 (MAIN / SUB)", example = "MAIN")
    private String custodyType;

    @JsonProperty("day_from_birth")
    @Schema(description = "출생 이후 경과한 일수", example = "456")
    private Long dayFromBirth;

    @Schema(description = "아기의 성별 (MALE / FEMALE)", example = "MALE")
    private Sex sex;
}
