package com.agarang.domain.chat_room;

import com.agarang.domain.chat_room.dto.response.MessageResponse;
import com.agarang.domain.chat_room.entity.ChatRoom;
import com.agarang.domain.chat_room.service.ChatRoomService;
import com.agarang.domain.chat_room.service.ChattingService;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import com.agarang.global.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.Objects;

/**
 * packageName    : com.agarang.domain.chat_room<br>
 * fileName       : WebSocketEventListener.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : WebSocket 이벤트를 처리하는 리스너 클래스입니다.
 * * <p>사용자의 WebSocket 연결 이벤트를 감지하고, 새로운 사용자 입장 시 환영 메시지를 생성하여 전송합니다.</p><br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChattingService chattingService;

    /**
     * 사용자의 WebSocket 연결 이벤트를 처리합니다.
     *
     * <p>사용자가 WebSocket을 통해 서버에 연결될 때 실행되며, 인증을 수행하고 사용자가 처음 입장하는 경우
     * 채팅방에 참여시킨 후 입장 메시지를 전송합니다.</p>
     *
     * <h3>예상 시나리오</h3>
     * <ul>
     *     <li>사용자가 WebSocket 연결 요청을 보냅니다.</li>
     *     <li>헤더에서 토큰을 추출하여 사용자 ID를 검증합니다.</li>
     *     <li>채팅방 ID(`baby_id`)를 확인하고 해당 채팅방을 찾습니다.</li>
     *     <li>사용자가 처음 입장하는 경우, 채팅방에 추가 후 입장 메시지를 전송합니다.</li>
     * </ul>
     *
     * <h3>예외 처리</h3>
     * <ul>
     *     <li>인증 실패 시 클라이언트에게 오류 메시지를 전송합니다.</li>
     *     <li>유효하지 않은 `baby_id` 값이 포함된 경우 예외를 발생시킵니다.</li>
     * </ul>
     *
     * @param event WebSocket 연결 이벤트 {@link SessionConnectedEvent}
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Message<?> connectMessage = (Message<?>) event.getMessage().getHeaders().get("simpConnectMessage");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(connectMessage);
        try {
            Integer userId = validationAuthorization(headerAccessor);
            String chatRoomIdHeader = headerAccessor.getFirstNativeHeader("baby_id");

            Integer babyId = Integer.parseInt(chatRoomIdHeader);

            ChatRoom chatRoom = chatRoomService.findChatRoomByBabyId(babyId);
            User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            if (chatRoomService.isFirstTimeEntry(user, chatRoom)) {
                chatRoomService.joinChatRoom(user, chatRoom);

                MessageResponse firstJoinMessage = chattingService.firstJoinMessage(user, chatRoom);

                messagingTemplate.convertAndSend("/topic/chat-room/" + chatRoom.getBaby().getBabyId(), firstJoinMessage);
            }
        } catch (Exception e) {
            sendErrorMessageToUser(headerAccessor, e.getMessage());
        }
    }

    /**
     * WebSocket 연결 요청의 인증 헤더를 검증하여 사용자 ID를 반환합니다.
     *
     * <p>헤더에서 `Authorization` 토큰을 추출하고 검증하여 사용자 ID를 파싱합니다.</p>
     *
     * <h3>예제 요청 헤더</h3>
     * <pre>
     * Authorization: Bearer eyJhbGciOiJIUzI1...
     * </pre>
     *
     * @param headerAccessor STOMP 헤더 액세서 {@link StompHeaderAccessor}
     * @return 검증된 사용자 ID
     * @throws BusinessException - 토큰이 없거나 유효하지 않을 경우 {@link ErrorCode#INVALID_TOKEN}, {@link ErrorCode#UNAUTHORIZED}
     */
    private Integer validationAuthorization(StompHeaderAccessor headerAccessor) {
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

    /**
     * WebSocket 클라이언트에게 오류 메시지를 전송합니다.
     *
     * <p>세션 ID를 기반으로 특정 사용자에게 개인 큐(`/queue/errors`)를 통해 오류 메시지를 전송합니다.</p>
     *
     * <h3>예제 응답</h3>
     * <pre>
     * {
     *     "error": "유효하지 않은 요청입니다."
     * }
     * </pre>
     *
     * @param headerAccessor STOMP 헤더 액세서 {@link StompHeaderAccessor}
     * @param errorMessage   전송할 오류 메시지
     */
    private void sendErrorMessageToUser(StompHeaderAccessor headerAccessor, String errorMessage) {
        String body = "{\"error\": \"" + errorMessage + "\"}";
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/errors",
                    body
            );
        }
    }
}
