package com.agarang.domain.chat_room.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.chat_room.dto.request.ChattingRequest;
import com.agarang.domain.chat_room.dto.response.MessageResponse;
import com.agarang.domain.chat_room.entity.ChatRoom;
import com.agarang.domain.chat_room.entity.Chatting;
import com.agarang.domain.chat_room.entity.ChattingType;
import com.agarang.domain.chat_room.repository.ChatRoomRepository;
import com.agarang.domain.chat_room.repository.ChattingRepository;
import com.agarang.domain.chat_room.repository.UserChatRoomRepository;
import com.agarang.domain.custody.entity.Custody;
import com.agarang.domain.custody.repository.CustodyRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.repository.UserRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.agarang.domain.chat_room.service<br>
 * fileName       : ChattingService.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chatting 에 대한 비즈니스 로직이 있는 service 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ChattingService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChattingRepository chattingRepository;
    private final CustodyRepository custodyRepository;
    private final BabyRepository babyRepository;

    private static final String FIRST_JOIN_MESSAGE = "님이 들어왔습니다.";

    /**
     * 채팅 메시지를 생성하고 저장합니다.
     *
     * <p>사용자가 특정 채팅방에서 메시지를 전송하면, 이 메시지를 저장한 후 해당 메시지 정보를 반환합니다.</p>
     *
     * <h3>예시 요청:</h3>
     * <pre>
     * {
     *     "content": "안녕하세요!",
     *     "chattingType": "TEXT"
     * }
     * </pre>
     *
     * <h3>예시 응답:</h3>
     * <pre>
     * {
     *     "chattingId": 1,
     *     "userId": 101,
     *     "userName": "홍길동",
     *     "custodyType": "MAIN",
     *     "userImage": "/images/user101.png",
     *     "content": "안녕하세요!",
     *     "createdAt": "2025-02-18T12:34:56",
     *     "chattingType": "TEXT"
     * }
     * </pre>
     *
     * @param userId          메시지를 보낸 사용자 ID
     * @param babyId          채팅이 속한 아기의 ID
     * @param chattingRequest 메시지 요청 객체 {@link ChattingRequest}
     * @return 생성된 메시지 정보를 포함한 {@link MessageResponse}
     * @throws BusinessException - 사용자 또는 아기가 존재하지 않을 경우 {@link ErrorCode#USER_NOT_FOUND}, {@link ErrorCode#BABY_NOT_FOUND}
     *                           - 사용자가 채팅방에 참여하지 않은 경우 {@link ErrorCode#USER_CHATROOM_NOT_JOIN}
     *                           - 사용자의 양육권 정보가 없을 경우 {@link ErrorCode#CUSTODY_NOT_FOUND}
     */
    public MessageResponse createMessage(Integer userId, Integer babyId, ChattingRequest chattingRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BABY_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findByBaby(baby);

        if (!userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new BusinessException(ErrorCode.USER_CHATROOM_NOT_JOIN);
        }

        Chatting chatting = new Chatting(chattingRequest.getContent(), user, chatRoom, chattingRequest.getChattingType());
        Chatting savedChatting = chattingRepository.save(chatting);

        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, baby)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND));

        return new MessageResponse(
                savedChatting.getChattingId(),
                user.getUserId(),
                user.getName(),
                custody.getCustodyType(),
                user.getUserImage(),
                savedChatting.getContent(),
                savedChatting.getCreatedAt(),
                savedChatting.getChattingType()
        );
    }

    /**
     * 사용자가 채팅방에 처음 입장할 때 입장 메시지를 생성하고 저장합니다.
     *
     * <p>사용자가 채팅방에 처음 입장하면, "{사용자명}님이 들어왔습니다." 형식의 입장 메시지를 저장하고 반환합니다.</p>
     *
     * <h3>예시 응답:</h3>
     * <pre>
     * {
     *     "chattingId": 2,
     *     "userId": 102,
     *     "userName": "김철수",
     *     "custodyType": "SUB",
     *     "userImage": "/images/user102.png",
     *     "content": "김철수님이 들어왔습니다.",
     *     "createdAt": "2025-02-18T12:35:00",
     *     "chattingType": "JOIN"
     * }
     * </pre>
     *
     * @param user     입장한 사용자 {@link User}
     * @param chatRoom 사용자가 입장한 채팅방 {@link ChatRoom}
     * @return 생성된 입장 메시지 정보를 포함한 {@link MessageResponse}
     * @throws BusinessException - 사용자의 양육권 정보가 없을 경우 {@link ErrorCode#CUSTODY_NOT_FOUND}
     */
    public MessageResponse firstJoinMessage(User user, ChatRoom chatRoom) {
        Chatting chatting = new Chatting(user.getName() + FIRST_JOIN_MESSAGE, user, chatRoom, ChattingType.JOIN);
        Chatting saveChatting = chattingRepository.save(chatting);
        Custody custody = custodyRepository.findByUserAndBabyAndDeletedAtIsNull(user, chatRoom.getBaby())
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CUSTODY_NOT_FOUND)
                );

        return new MessageResponse(saveChatting.getChattingId(), user.getUserId(), user.getName(), custody.getCustodyType(), user.getUserImage(), saveChatting.getContent(), saveChatting.getCreatedAt(), saveChatting.getChattingType());
    }
}
