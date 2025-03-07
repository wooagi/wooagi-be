package com.agarang.domain.custody.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.custody.dto.response<br>
 * fileName       : CustodyInviteResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 31.<br>
 * description    :  Custody invite code response dto 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.31         Fiat_lux            최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "공동양육 초대 코드 응답 객체")
public class CustodyInviteResponse {

    @Schema(description = "생성된 초대 코드", example = "a1b2c3d4")
    @JsonProperty("invite_code")
    private String inviteCode;
}
