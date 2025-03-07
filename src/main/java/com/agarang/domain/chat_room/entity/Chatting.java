package com.agarang.domain.chat_room.entity;

import com.agarang.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.chat_room.entity<br>
 * fileName       : Chatting.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-02-19<br>
 * description    : chatting entity 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.19          Fiat_lux           최초생성<br>
 */
@Entity
@Table(name = "chatting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Chatting {

    @Id
    @Column(name = "chatting_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chattingId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "chatting_type", nullable = false, length = 6)
    private ChattingType chattingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;


    public Chatting(String content, User user, ChatRoom chatRoom, ChattingType chattingType) {
        this.content = content;
        this.user = user;
        this.chatRoom = chatRoom;
        this.chattingType = chattingType;
    }
}
