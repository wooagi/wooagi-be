package com.agarang.domain.baby.dto.response;

import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.baby.dto.response<br>
 * fileName       : BabyGetResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 4.<br>
 * description    :  아기에 대한 상세 조회 response dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.04          Fiat_lux           최초 생성<br>
 * <br>
 */
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "아기 상세 정보 응답 객체")
public class BabyGetResponse {

    @Schema(description = "아기의 ID", example = "1")
    private Integer babyId;

    @Schema(description = "아기의 프로필 이미지 URL", example = "https://s3.amazonaws.com/my-bucket/baby.jpg")
    private String babyImage;

    @Schema(description = "아기의 이름", example = "김아기")
    private String name;

    @Schema(description = "아기의 성별 (MALE / FEMALE)", example = "MALE")
    private Sex sex;

    @Schema(description = "아기의 생년월일 (YYYY-MM-DDTHH:MM:SS)", example = "2023-01-01T00:00:00")
    private LocalDateTime birth;

    @Schema(description = "보호자가 몇 명인지 나타내는 값", example = "2")
    private Integer custodyCount;

    @Schema(description = "아기의 몸무게 (kg)", example = "7.5")
    private BigDecimal weight;

    @Schema(description = "아기의 키 (cm)", example = "70.2")
    private BigDecimal height;

    @Schema(description = "아기의 머리 둘레 (cm)", example = "45.5")
    private BigDecimal headSize;
}
