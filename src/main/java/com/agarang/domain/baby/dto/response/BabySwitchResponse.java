package com.agarang.domain.baby.dto.response;

import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.baby.dto.response<br>
 * fileName       : BabySwitchResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 24.<br>
 * description    :  아기 변경 할때 아기 목록 response dto 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          Fiat_lux           최초 생성<br>
 * <br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "사용자가 관리하는 다른 아기 목록 응답 객체")
public class BabySwitchResponse {

    @Schema(description = "아기의 이름", example = "김아기")
    private String name;

    @Schema(description = "아기의 프로필 이미지 URL", example = "https://s3.amazonaws.com/my-bucket/baby.jpg")
    private String image;

    @Schema(description = "아기의 성별 (MALE / FEMALE)", example = "MALE")
    private Sex sex;

    @JsonProperty("day_from_birth")
    @Schema(description = "출생 이후 경과한 일수", example = "456")
    private Long dayFromBirth;
}
