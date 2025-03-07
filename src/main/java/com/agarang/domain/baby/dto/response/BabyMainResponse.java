package com.agarang.domain.baby.dto.response;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.user.entity.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * packageName    : com.agarang.domain.baby.dto.response<br>
 * fileName       : BabyMainResponse.java<br>
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
@Schema(description = "아기 주요 정보 응답 객체")
public class BabyMainResponse {
    @JsonProperty("baby_id")
    @Schema(description = "아기의 ID", example = "1")
    private Integer babyId;

    @Schema(description = "아기의 프로필 이미지 URL", example = "https://s3.amazonaws.com/my-bucket/baby.jpg")
    private String image;

    @Schema(description = "아기의 이름", example = "김아기")
    private String name;

    @Schema(description = "아기의 생년월일 (YYYY-MM-DDTHH:MM:SS)", example = "2023-01-01T00:00:00")
    private LocalDateTime birth;

    @Schema(description = "아기의 성별 (MALE / FEMALE)", example = "MALE")
    private Sex sex;

    @Schema(description = "아기의 실제 나이 (만 나이)", example = "1")
    private Integer age;

    @JsonProperty("korean_age")
    @Schema(description = "아기의 한국식 나이", example = "2")
    private Integer koreanAge;

    @JsonProperty("day_from_month")
    @Schema(description = "출생 이후 경과한 개월 수", example = "15")
    private Integer dayFromMonth;

    @JsonProperty("day_from_birth")
    @Schema(description = "출생 이후 경과한 일수", example = "456")
    private Long dayFromBirth;

    @JsonProperty("another_baby_list")
    @Schema(description = "사용자가 관리 중인 다른 아기 목록", example = "[]")
    private List<BabySwitchResponse> anotherBabyList;

    public static BabyMainResponse fromEntity(Baby baby, List<BabySwitchResponse> anotherBabyList) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = baby.getBirth().toLocalDate();


        int age = Period.between(birthDate, today).getYears();

        int koreanAge = today.getYear() - baby.getBirth().getYear() + 1;

        int birthMonth = Period.between(birthDate, today).getYears() * 12
                + Period.between(birthDate, today).getMonths();

        long birthDay = ChronoUnit.DAYS.between(birthDate, today);

        return new BabyMainResponse(
                baby.getBabyId(),
                baby.getBabyImage(),
                baby.getName(),
                baby.getBirth(),
                baby.getSex(),
                age,
                koreanAge,
                birthMonth,
                birthDay,
                anotherBabyList
        );
    }
}
