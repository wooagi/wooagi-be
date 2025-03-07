package com.agarang.domain.custody.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.domain.custody.dto.response<br>
 * fileName       : CustodyResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 1. 22.<br>
 * description    :  Custody entity 의 response dto 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.22         Fiat_lux            최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "공동양육자 목록 응답 객체")
public class CustodyResponse {

    @Schema(description = "공동양육자 사용자 ID", example = "101")
    @JsonProperty("user_id")
    private Integer userId;

    @Schema(description = "공동양육자 프로필 이미지 URL", example = "https://s3.amazonaws.com/user/profile.jpg")
    @JsonProperty("user_image")
    private String userImage;

    @Schema(description = "공동양육자 이름", example = "홍길동")
    private String name;

    @Schema(description = "공동양육자 유형 (MAIN / SUB)", example = "MAIN")
    @JsonProperty("custody_type")
    private String custodyType;
}