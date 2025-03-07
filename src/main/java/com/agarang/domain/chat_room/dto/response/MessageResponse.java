package com.agarang.domain.chat_room.dto.response;

import com.agarang.domain.chat_room.entity.ChattingType;
import com.agarang.domain.custody.entity.CustodyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.chat_room.dto.response<br>
 * fileName       : MessageResponse.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : 메세지 response dto 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Getter
@AllArgsConstructor
@Schema(description = "채팅 메시지 응답 객체")
public class MessageResponse {

    @Schema(description = "채팅 메시지 ID", example = "1001")
    @JsonProperty("chatting_id")
    private Integer chattingId;

    @Schema(description = "사용자 ID", example = "123")
    @JsonProperty("user_id")
    private Integer userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "사용자의 보호자 타입 (MAIN / SUB)", example = "MAIN")
    @JsonProperty("custody_type")
    private CustodyType custodyType;

    @Schema(description = "사용자 프로필 이미지 URL", example = "https://s3.amazonaws.com/user/profile.jpg")
    @JsonProperty("user_image")
    private String userImage;

    @Schema(description = "채팅 메시지 내용", example = "안녕하세요!")
    private String content;

    @Schema(description = "메시지 생성 시간", example = "2025-02-19T12:34:56")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "채팅 메시지 타입 (JOIN / NORMAL / EMOJI)", example = "NORMAL")
    @JsonProperty("chatting_type")
    private ChattingType chattingType;
}
