package com.agarang.domain.chat_room.dto.request;

import com.agarang.domain.chat_room.entity.ChattingType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.agarang.domain.chat_room.dto.request<br>
 * fileName       : ChattingRequest.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chatting request dto 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 전송 요청 객체")
public class ChattingRequest {
    @JsonProperty("chatting_type")
    @Schema(description = "채팅 메시지 타입 (JOIN / NORMAL / EMOJI)", example = "NORMAL")
    private ChattingType chattingType;

    @Schema(description = "채팅 메시지 내용", example = "안녕하세요!")
    @NotBlank
    private String content;
}
