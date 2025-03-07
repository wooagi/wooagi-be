package com.agarang.domain.chat_room.repository;

import com.agarang.domain.chat_room.entity.ChatRoom;
import com.agarang.domain.chat_room.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.agarang.domain.chat_room.repository<br>
 * fileName       : ChattingRepository.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chatting entity 의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
public interface ChattingRepository extends JpaRepository<Chatting, Integer> {

    List<Chatting> findByChatRoomAndCreatedAtAfterOrderByCreatedAtAsc(ChatRoom chatRoom, LocalDateTime createdAt);
}
