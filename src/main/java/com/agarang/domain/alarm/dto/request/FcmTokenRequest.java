package com.agarang.domain.alarm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * packageName    : com.agarang.domain.alarm.dto.request<br>
 * fileName       : FcmTokenRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 11.<br>
 * description    :  fcm token request dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.05          Fiat_lux            최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "FCM 토큰 등록 요청 객체")
public class FcmTokenRequest {

    @Schema(description = "FCM 토큰 값", example = "abc123xyz456")
    @NotBlank
    @Length(min = 1, max = 255)
    private String token;
}
