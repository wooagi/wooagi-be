package com.agarang.domain.chat_room.service;

import com.agarang.domain.alarm.event.FcmTokenSendMessageEvent;
import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.chat_room.dto.request.ChatNoticeRequest;
import com.agarang.domain.chat_room.dto.response.ChatRoomMessageListResponse;
import com.agarang.domain.chat_room.dto.response.MessageResponse;
import com.agarang.domain.chat_room.entity.ChatRoom;
import com.agarang.domain.chat_room.entity.Chatting;
import com.agarang.domain.chat_room.entity.UserChatRoom;
import com.agarang.domain.chat_room.repository.ChatRoomRepository;
import com.agarang.domain.chat_room.repository.ChattingRepository;
import com.agarang.domain.chat_room.repository.UserChatRoomRepository;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.entity.CustodyType;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.chat_room.service<br>
 * fileName       : ChatRoomService.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chat_room 에 대한 비즈니스 로직이 있는 service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;
    private final BabyRepository babyRepository;
    private final ChattingRepository chattingRepository;
    private final CustodyRepository custodyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public static final String NOTIFICATION_MESSAGE = "\uD83D\uDEA8 삐용 삐용! \uD83D\uDEA8";
    public static final String NOTIFICATION_CONTENT_MESSAGE = "아기의 공지가 새로 등록 되었습니다.";

    /**
     * 새로운 채팅방을 생성합니다.
     *
     * <p>채팅방은 특정 아기와 연결 됩니다</p>
     *
     * @param baby 채팅방과 연결될 아기 {@link Baby}
     */
    public void saveChatRoom(Baby baby) {

        ChatRoom chatRoom = new ChatRoom(baby);
        chatRoomRepository.save(chatRoom);
    }

    /**
     * 사용자가 특정 채팅방에 처음 입장하는지 여부를 확인합니다.
     *
     * @param user     확인할 사용자 {@link User}
     * @param chatRoom 확인할 채팅방 {@link ChatRoom}
     * @return 처음 입장하는 경우 true, 아니면 false
     */
    @Transactional(readOnly = true)
    public boolean isFirstTimeEntry(User user, ChatRoom chatRoom) {
        return !userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom);
    }

    /**
     * 특정 아기의 ID를 기반으로 채팅방을 조회합니다.
     *
     * @param babyId 조회할 아기의 ID
     * @return 해당 아기의 채팅방 {@link ChatRoom}
     * @throws BusinessException - 아기가 존재하지 않을 경우 {@link ErrorCode#BABY_NOT_FOUND}
     */
    @Transactional(readOnly = true)
    public ChatRoom findChatRoomByBabyId(Integer babyId) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.BABY_NOT_FOUND)
                );
        return chatRoomRepository.findByBaby(baby);
    }

    /**
     * 사용자가 특정 채팅방에 참여합니다.
     *
     * @param user     참여할 사용자 {@link User}
     * @param chatRoom 참여할 채팅방 {@link ChatRoom}
     * @throws BusinessException - 이미 참여 중인 경우 {@link ErrorCode#BABY_NOT_FOUND}
     */
    public void joinChatRoom(User user, ChatRoom chatRoom) {
        if (userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new BusinessException(ErrorCode.BABY_NOT_FOUND);
        }

        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
    }

    /**
     * 특정 아기의 채팅 메시지를 조회합니다.
     *
     * <p>사용자가 채팅방에 참여한 이후의 메시지만 조회됩니다.</p>
     *
     * <h3>예시 응답:</h3>
     * <pre>
     * {
     *     "notice": "다음 주 월요일에 공지사항이 있습니다.",
     *     "messages": [
     *         {
     *             "chattingId": 1,
     *             "userId": 101,
     *             "userName": "홍길동",
     *             "custodyType": "MAIN",
     *             "userImage": "/images/user101.png",
     *             "content": "안녕하세요!",
     *             "createdAt": "2025-02-18T12:34:56",
     *             "chattingType": "TEXT"
     *         }
     *     ]
     * }
     * </pre>
     *
     * @param babyId 채팅방이 속한 아기의 ID
     * @param userId 조회하는 사용자 ID
     * @return 채팅 메시지 목록을 포함한 {@link ChatRoomMessageListResponse}
     * @throws BusinessException - 아기 또는 사용자 정보가 없을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public ChatRoomMessageListResponse getMessageResponse(Integer babyId, Integer userId) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findByBaby(baby);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_CHATROOM_NOT_FOUND));

        List<MessageResponse> responseList = chattingRepository
                .findByChatRoomAndCreatedAtAfterOrderByCreatedAtAsc(chatRoom, userChatRoom.getCreatedAt())
                .stream()
                .map(chatting -> {
                    User sender = chatting.getUser();
                    Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(sender, baby)
                            .orElse(null);

                    return new MessageResponse(
                            chatting.getChattingId(),
                            sender.getUserId(),
                            sender.getName(),
                            Objects.isNull(custody) ? CustodyType.LOSS : custody.getCustodyType(),
                            sender.getUserImage(),
                            chatting.getContent(),
                            chatting.getCreatedAt(),
                            chatting.getChattingType()
                    );
                }).collect(Collectors.toList());

        return new ChatRoomMessageListResponse(chatRoom.getNotice(), responseList);
    }

    /**
     * 특정 채팅방의 공지를 업데이트합니다.
     *
     * <p>공지 메시지는 채팅방에 있는 기존 메시지 중 하나로 설정됩니다.</p>
     *
     * <h3>예시 요청:</h3>
     * <pre>
     *     PUT /api/chat-rooms/{babyId}/notice
     *     Content-Type: application/json
     *
     *     {
     *         "messageId": 12
     *     }
     * </pre>
     *
     * @param babyId            공지를 수정할 아기의 ID
     * @param chatNoticeRequest 공지 수정 요청 데이터 {@link ChatNoticeRequest}
     * @throws BusinessException - 아기 또는 공지 메시지가 존재하지 않을 경우 예외 발생
     */
    public void updateNoticeMessage(Integer babyId, ChatNoticeRequest chatNoticeRequest) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findByBaby(baby);

        Chatting chatting = chattingRepository.findById(chatNoticeRequest.getMessageId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATTING_NOT_FOUND));

        chatRoom.setNotice(chatting.getContent());

        eventPublisher.publishEvent(new FcmTokenSendMessageEvent(
                this,
                baby.getBabyId(),
                NOTIFICATION_MESSAGE,
                baby.getName() + NOTIFICATION_CONTENT_MESSAGE
        ));
    }


}
