package com.agarang.domain.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.alarm.dto.request<br>
 * fileName       : NotificationJobRegister.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 6.<br>
 * description    :  알림 작업 설정 register request dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.06           Fiat_lux          최초생성<br>
 */
@Getter
@NoArgsConstructor
public class NotificationJobRegister {
    @NotNull
    @Min(1)
    @JsonProperty("baby_id")
    private Integer babyId;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    @JsonProperty("scheduled_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime scheduledAt;
}
