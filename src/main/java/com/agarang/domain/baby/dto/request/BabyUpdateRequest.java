package com.agarang.domain.baby.dto.request;

import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.baby.dto.response<br>
 * fileName       : null.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  아기 수정 요청 처리하는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "아기 정보 수정 요청 객체")
public class BabyUpdateRequest {

    @Schema(description = "수정할 아기의 ID", example = "1")
    @NotNull
    @Min(1)
    @JsonProperty("baby_id")
    private Integer babyId;

    @Schema(description = "새로운 아기의 이름", example = "김아기")
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    @Schema(description = "새로운 아기의 생년월일 (YYYY-MM-DD HH:mm:ss)", example = "2023-01-01 12:01:01")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birth;

    @Schema(description = "새로운 아기의 성별 (MALE / FEMALE)", example = "MALE")
    @NotNull
    private Sex sex;

    @Schema(description = "기존 이미지 URL (변경되지 않을 경우 그대로 유지)", example = "https://s3.amazonaws.com/my-bucket/baby.jpg")
    @Size(min = 2, max = 255)
    @JsonProperty("before_image")
    private String beforeImage;
}
