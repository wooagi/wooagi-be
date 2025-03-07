package com.agarang.domain.chat_room.controller;

import com.agarang.domain.chat_room.dto.request.ChattingRequest;
import com.agarang.domain.chat_room.dto.response.MessageResponse;
import com.agarang.domain.chat_room.service.ChattingService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.util.JwtUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * packageName    : com.agarang.domain.chat_room.controller<br>
 * fileName       : ChattingController.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : 채팅 기능을 처리하는 WebSocket 컨트롤러입니다.
 * <p>이 컨트롤러는 STOMP(WebSocket) 메시지를 처리하고, 채팅 메시지를 주고받는 기능을 제공합니다.</p><br>
 * <p>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Controller
@RequiredArgsConstructor
@Tag(name = "채팅 API", description = "채팅 메시지를 WebSocket을 통해 전송하는 API")
public class ChattingController {
    private final ChattingService chattingService;
    private final JwtUtility jwtUtility;

    /**
     * 특정 아기의 채팅방에서 메시지를 전송합니다.
     *
     * <p>클라이언트가 `/chat-room/{babyId}/sendMessage`로 메시지를 전송하면,
     * 해당 메시지를 처리하여 `/topic/chat-room/{babyId}` 주제로 브로드캐스트합니다.</p>
     *
     * <h3>예시 STOMP 요청:</h3>
     * <pre>
     *     {
     *         "content": "안녕하세요!",
     *         "timestamp": "2025-02-18T12:34:56"
     *     }
     * </pre>
     *
     * <h3>예시 STOMP 응답:</h3>
     * <pre>
     *     {
     *         "sender": "사용자1",
     *         "content": "안녕하세요!",
     *         "timestamp": "2025-02-18T12:34:56"
     *     }
     * </pre>
     *
     * @param babyId          채팅방이 속한 아기의 ID
     * @param chattingRequest 전송할 채팅 메시지 {@link ChattingRequest}
     * @param headerAccessor  STOMP 메시지 헤더
     * @return 메시지 응답 {@link MessageResponse}
     * @throws BusinessException - 토큰이 유효하지 않을 경우 {@link ErrorCode#INVALID_TOKEN}
     *                           - 인증되지 않은 사용자인 경우 {@link ErrorCode#UNAUTHORIZED}
     */
    @Operation(summary = "채팅 메시지 전송", description = "사용자가 특정 채팅방에서 메시지를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 메시지가 성공적으로 전송됨",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "사용자에게 권한이 없음",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방 또는 사용자",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @MessageMapping("/chat-room/{babyId}/sendMessage")
    @SendTo("/topic/chat-room/{babyId}")
    public MessageResponse sendMessage(@Parameter(description = "채팅 메시지를 전송할 아기의 ID", required = true) @DestinationVariable Integer babyId,
                                       ChattingRequest chattingRequest,
                                       SimpMessageHeaderAccessor headerAccessor) {

        Integer userId = validationAuthorization(headerAccessor);
        return chattingService.createMessage(userId, babyId, chattingRequest);
    }

    /**
     * STOMP 메시지 헤더에서 Authorization 토큰을 검증하여 사용자 ID를 추출합니다.
     *
     * <p>Authorization 헤더에서 JWT를 추출한 후, 토큰을 검증하여 사용자 ID를 반환합니다.
     * 유효하지 않은 토큰이거나 헤더가 존재하지 않으면 예외가 발생합니다.</p>
     *
     * @param headerAccessor STOMP 메시지 헤더
     * @return 검증된 사용자 ID
     * @throws BusinessException - 토큰이 유효하지 않을 경우 {@link ErrorCode#INVALID_TOKEN}
     *                           - 인증되지 않은 사용자인 경우 {@link ErrorCode#UNAUTHORIZED}
     */
    private Integer validationAuthorization(SimpMessageHeaderAccessor headerAccessor) {
        String authorizationHeader = headerAccessor.getFirstNativeHeader("Authorization");

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                return Integer.parseInt(jwtUtility.getUserId(token));
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }
        }

        throw new BusinessException(ErrorCode.UNAUTHORIZED);
    }
}
