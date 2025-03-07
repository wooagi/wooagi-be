package com.agarang.domain.alarm.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * packageName    : com.agarang.domain.alarm.event<br>
 * fileName       : FcmTokenSendMessageEvent.java<br>
 * author         : Fiat_lux<br>
 * date           : 25. 2. 5.<br>
 * description    :  Fcm token 으로 메세지 보내는 event 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.05          Fiat_lux           최초생성<br>
 */
@Getter
public class FcmTokenSendMessageEvent extends ApplicationEvent {
    private final Integer babyId;
    private final String message;
    private final String title;

    public FcmTokenSendMessageEvent(Object source, Integer babyId, String title, String message) {
        super(source);
        this.babyId = babyId;
        this.title = title;
        this.message = message;
    }
}
