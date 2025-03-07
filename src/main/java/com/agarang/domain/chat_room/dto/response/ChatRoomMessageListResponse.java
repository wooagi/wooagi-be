package com.agarang.domain.chat_room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * packageName    : com.agarang.domain.chat_room.dto.response<br>
 * fileName       : ChatRoomMessageListResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    :  채팅방의 메세지 리스트 response dto 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "채팅방 메시지 목록 응답 객체")
public class ChatRoomMessageListResponse {

    @Schema(description = "공지 메시지", example = "이번 주 금요일에 단체 회의가 있습니다.")
    @JsonProperty("notice_message")
    private String noticeMessage;

    @Schema(description = "채팅 메시지 목록")
    @JsonProperty("message_response")
    private List<MessageResponse> messageResponses;
}