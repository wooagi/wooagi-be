package com.agarang.domain.auth.dto.request;

import com.agarang.domain.user.entity.Sex;
import com.agarang.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.auth.dto.request<br>
 * fileName       : KakaoLoginRequest.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 22.<br>
 * description    : 카카오 로그인 응답 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22          okeio           최초 생성<br>
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoLoginRequest {

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank
    private String name;

    @Schema(description = "이메일 주소", example = "user@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "출생 연도 (YYYY 형식)", example = "1990")
    @Pattern(regexp = "\\d{4}")
    private String birthyear;

    @Schema(description = "생년월일 (MMDD 형식)", example = "0523")
    @Pattern(regexp = "\\d{4}")
    private String birthday;

    @Schema(description = "생년월일 (MMDD 형식)", example = "0523")
    @NotBlank
    private String gender;

    @Schema(description = "소셜 로그인 제공자의 ID", example = "123456789")
    @NotBlank
    private String providerId;


    /**
     * KakaoLoginRequest DTO를 User 엔터티로 변환합니다.
     *
     * <p>
     * 카카오 유저 정보를 받아와 User 엔터티로 변환합니다.
     * </p>
     *
     * @return 회원 {@link User} 객체
     */
    public User toUserEntity() {
        return User.builder()
                .name(name)
                .birth(LocalDate.of(
                        Integer.parseInt(birthyear),
                        Integer.parseInt(birthday.substring(0, 2)),
                        Integer.parseInt(birthday.substring(2))
                ))
                .email(email)
                .sex(Sex.valueOf(gender.toUpperCase()))
                .createdAt(LocalDateTime.now())
                .providerId(providerId)
                .build();
    }
}
