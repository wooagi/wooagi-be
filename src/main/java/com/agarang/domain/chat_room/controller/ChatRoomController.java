package com.agarang.domain.chat_room.controller;

import com.agarang.domain.chat_room.dto.request.ChatNoticeRequest;
import com.agarang.domain.chat_room.dto.response.ChatRoomMessageListResponse;
import com.agarang.domain.chat_room.service.ChatRoomService;
import com.agarang.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * packageName    : com.agarang.domain.chat_room.controller<br>
 * fileName       : ChatRoomController.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    :  chat_room entity 의 api controller 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
@Tag(name = "채팅방 API", description = "채팅방 메시지 및 공지를 관리하는 API")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    /**
     * 특정 아기의 채팅방 메시지를 조회합니다.
     *
     * <p>이 메서드는 사용자의 인증 정보를 기반으로 특정 아기 ID에 해당하는 채팅방 메시지를 조회하여 반환합니다.</p>
     *
     * <h3>예시 요청:</h3>
     * <pre>
     *     GET /api/chat-rooms/{babyId}
     * </pre>
     *
     * <h3>예시 응답:</h3>
     * <pre>
     * {
     *     "messages": [
     *         {
     *             "sender": "사용자1",
     *             "content": "안녕하세요!",
     *             "timestamp": "2024-02-18T12:34:56"
     *         },
     *         {
     *             "sender": "사용자2",
     *             "content": "안녕하세요! 반갑습니다.",
     *             "timestamp": "2024-02-18T12:35:10"
     *         }
     *     ]
     * }
     * </pre>
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param babyId            조회할 아기의 ID
     * @return 채팅 메시지 목록을 포함한 {@link ChatRoomMessageListResponse}재하지 않거나 권한이 없을 경우 예외 발생
     */
    @Operation(summary = "채팅방 메시지 조회", description = "특정 아기의 채팅방 메시지를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 메시지 조회 성공",
                    content = @Content(schema = @Schema(implementation = ChatRoomMessageListResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기 또는 채팅방",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{babyId}")
    public ResponseEntity<ChatRoomMessageListResponse> getChatRoomMessage(@Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                          @Parameter(description = "조회할 아기의 ID", required = true) @PathVariable Integer babyId) {

        int userId = customUserDetails.getUserId();
        ChatRoomMessageListResponse messageResponse = chatRoomService.getMessageResponse(babyId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    /**
     * 특정 아기의 채팅방 공지를 수정합니다.
     *
     * <p>이 메서드는 관리자가 채팅방 공지를 업데이트할 수 있도록 합니다.</p>
     *
     * <h3>예시 요청:</h3>
     * <pre>
     *     PUT /api/chat-rooms/{babyId}
     *     Content-Type: application/json
     *
     *     {
     *         "noticeMessage": "이번 주 금요일에 단체 회의가 있습니다."
     *     }
     * </pre>
     *
     * <h3>응답:</h3>
     * <pre>
     *     HTTP/1.1 201 Created
     * </pre>
     *
     * @param babyId            공지를 수정할 아기의 ID
     * @param chatNoticeRequest 공지 수정 요청 데이터 {@link ChatNoticeRequest}
     * @return HTTP 201 응답 (성공적으로 공지 수정됨)
     */
    @Operation(summary = "채팅방 공지 수정", description = "특정 아기의 채팅방 공지를 수정합니다. (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "공지 메시지가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "사용자에게 관리자 권한이 없음",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 아기 또는 채팅 메시지",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{babyId}")
    public ResponseEntity<Void> updateNotice(@Parameter(description = "공지 메시지를 수정할 아기의 ID", required = true) @PathVariable Integer babyId,
                                             @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                     schema = @Schema(implementation = ChatNoticeRequest.class)
                                             )) ChatNoticeRequest chatNoticeRequest) {

        chatRoomService.updateNoticeMessage(babyId, chatNoticeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
