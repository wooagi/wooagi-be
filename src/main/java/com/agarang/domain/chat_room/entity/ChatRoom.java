package com.agarang.domain.chat_room.entity;

import com.agarang.domain.baby.entity.Baby;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.agarang.domain.chat_room.entity<br>
 * fileName       : ChatRoom.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chat_room entity 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Entity
@Table(name = "chat_room")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatRoomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baby_id")
    private Baby baby;

    @Column(columnDefinition = "TEXT")
    private String notice;

    public ChatRoom(Baby baby) {
        this.baby = baby;
    }
}
