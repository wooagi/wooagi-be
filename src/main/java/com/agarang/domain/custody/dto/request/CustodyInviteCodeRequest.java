package com.agarang.domain.custody.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.custody.dto.request<br>
 * fileName       : CustodyInviteCodeRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 31.<br>
 * description    :  Custody invite code request dto 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.31         Fiat_lux            최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "공동양육 초대 코드 검증 요청 객체")
public class CustodyInviteCodeRequest {

    @Schema(description = "초대 코드 (8자리)", example = "a1b2c3d4")
    @JsonProperty("invite_code")
    @NotBlank(message = "초대 코드는 필수 값입니다.")
    @Size(min = 8, max = 8, message = "초대 코드는 8자리여야 합니다.")
    private String inviteCode;
}
