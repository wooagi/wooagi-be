package com.agarang.domain.chat_room.repository;

import com.agarang.domain.chat_room.entity.ChatRoom;
import com.agarang.domain.chat_room.entity.UserChatRoom;
import com.agarang.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.agarang.domain.chat_room.repository<br>
 * fileName       : UserChatRoomRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : user_chat_room entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Integer> {
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);
}
