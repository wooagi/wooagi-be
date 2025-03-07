package com.agarang.domain.custody.dto.request;

import com.agarang.domain.custody.entity.CustodyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.custody.dto.request<br>
 * fileName       : CustodyInviteRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 31.<br>
 * description    :  Custody invite code 생성 request dto 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.31         Fiat_lux            최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "공동양육 초대 코드 요청 객체")
public class CustodyInviteRequest {

    @Schema(description = "초대할 아기의 ID", example = "123")
    @Min(0)
    @NotNull
    @JsonProperty("baby_id")
    private Integer babyId;

    @Schema(description = "공동양육자 유형 (MAIN / SUB)", example = "SUB")
    @NotNull
    @JsonProperty("custody_type")
    private CustodyType custodyType;
}
