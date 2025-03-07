package com.agarang.domain.record.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * packageName    : com.agarang.domain.record.dto.response<br>
 * fileName       : AntipyreticCheckResponse.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 해열제 복용 가능 여부 응답 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Schema(description = "해열제 복용 가능 여부 응답 DTO")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AntipyreticCheckResponse (
        @Schema(description = "나이에 따른 복용 가능 여부", example = "true")
        boolean isAgeSafe,

        @Schema(description = "최근 2시간 이내 복용 여부", example = "true")
        boolean is2hPassed,

        @Schema(description = "같은 성분을 복용한 후 4시간이 지났는지 여부", example = "false")
        boolean is4hPassedSinceSameDoes,

        @Schema(description = "1회 복용량이 안전한지 여부", example = "true")
        boolean isDoseSafe,

        @Schema(description = "일일 복용량이 안전한지 여부", example = "false")
        boolean isDailySafe,

        @Schema(description = "나이에 따른 복용 가능 여부 메시지")
        String isAgeSafeMessage,

        @Schema(description = "최근 2시간 이내 복용 여부 메세지")
        String is2hPassedMessage,

        @Schema(description = "같은 성분을 복용한 후 4시간이 지났는지 여부 메세지")
        String is4hPassedSinceSameDoesMessage,

        @Schema(description = "1회 복용량 안전 여부 메세지")
        String isDoseSafeMessage,

        @Schema(description = "일일 복용량 안전 여부 메세지")
        String isDailySafeMessage

) {}
