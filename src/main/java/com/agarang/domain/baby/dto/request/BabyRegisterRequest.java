package com.agarang.domain.baby.dto.request;

import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.baby.dto.request<br>
 * fileName       : BabyRegisterRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  아기 등록 요청 처리하는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          Fiat_lux           최초 생성<br>
 * 25.01.31          Fiat_lux           uploadImage 필드 삭제<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "아기 등록 요청 객체")
public class BabyRegisterRequest {

    @Schema(description = "아기의 이름", example = "김아기")
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    @Schema(description = "아기의 생년월일", example = "2023-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime birth;

    @Schema(description = "아기의 성별 (MALE / FEMALE)", example = "MALE")
    @NotNull
    private Sex sex;
}
