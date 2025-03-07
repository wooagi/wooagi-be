package com.agarang.domain.chat_room.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.agarang.domain.chat_room.dto.request<br>
 * fileName       : ChatNoticeRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chatting 공지 수정 request dto 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Getter
@NoArgsConstructor
@Schema(description = "채팅방 공지 수정 요청 객체")
public class ChatNoticeRequest {

    @Schema(description = "공지로 설정할 메시지 ID", example = "1001")
    @JsonProperty("message_id")
    private Integer messageId;
}
