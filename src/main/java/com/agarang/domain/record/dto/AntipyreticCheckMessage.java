package com.agarang.domain.record.dto;

/**
 * packageName    : com.agarang.domain.record.dto<br>
 * fileName       : AntipyreticCheckMessage.java<br>
 * author         : nature1216 <br>
 * date           : 2/9/25<br>
 * description    : 해열제 복용 가능 여부 체크 메세지 enum 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/9/25          nature1216          최초생성<br>
 */
public enum AntipyreticCheckMessage {

    AGE_IS_NOT_SAFE("복용할 수 없는 개월수입니다."),
    HAS_TAKEN_WITHIN_2H("해열제를 복용한 지 2시간이 지나지 않았습니다."),
    HAS_TAKEN_SAME_WITHIN_4H("같은 성분의 해열제를 복용한 지 4시간이 지나지 않았습니다."),
    DOES_IS_NOT_SAFE("1회 복용량을 초과했습니다."),
    WEIGHT_RECORD_NOT_FOUND("체중 기록을 찾을 수 없습니다."),
    DAILY_AMOUNT_IS_NOT_SAFE("하루 복용량을 초과했습니다.");
    private String message;

    AntipyreticCheckMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
