package com.agarang.domain.chat_room.repository;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.chat_room.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.agarang.domain.chat_room.repository<br>
 * fileName       : ChatRoomRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chat_room entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findByBaby(Baby baby);
}
