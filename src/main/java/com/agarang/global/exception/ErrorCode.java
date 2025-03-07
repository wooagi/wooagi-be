package com.agarang.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.agarang.global.exception<br>
 * fileName       : ErrorCode.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 23.<br>
 * description    : Exception 관리를 위한 ENUM 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.23          okeio           최초생성<br>
 * 25.01.30          Fiat_lux           diary, custody, piyong <br>
 * 25.01.31          Fiat_lux           custody redis exception<br>
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "유효성 검증에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002", "서버에서 처리할 수 없습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다."),
    HAS_EMAIL(HttpStatus.BAD_REQUEST, "USER-002", "존재하는 이메일입니다."),
    USER_ALREADY_DELETED(HttpStatus.CONFLICT, "USER-003", "탈퇴한 회원입니다."),

    USER_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-001", "회원 가입 처리 중 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-002", "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-003", "유효하지 않은 토큰입니다."),
    TOKEN_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-004", "토큰 저장 중 오류가 발생했습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-005", "Redis 리프레시 토큰과 다릅니다."),

    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD-001", "기록을 찾을 수 없습니다."),
    DELETE_MODIFY_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "RECORD-002", "수정 및 삭제 권한이 없습니다."),
    UNSUPPORTED_RECORD_TYPE(HttpStatus.BAD_REQUEST, "RECORD-003", "존재하지 않는 카테고리입니다."),
    INVALID_MODIFY_REQUEST(HttpStatus.BAD_REQUEST, "RECORD-004", "잘못된 수정 요청입니다."),
    RECORD_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "RECORD-005", "지원하지 않는 카테고리입니다."),
    INVALID_CREATE_REQUEST(HttpStatus.BAD_REQUEST, "RECORD-006", "잘못된 생성 요청입니다"),

    BABY_NOT_FOUND(HttpStatus.NOT_FOUND, "BABY-001", "아기를 찾을 수 없습니다."),

    CUSTODY_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "CUSTODY-001", "해당 아기에 대한 권한이 없습니다."),
    CUSTODY_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTODY-001", "양육권이 없습니다."),
    CUSTODY_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "CUSTODY-002", "양육권을 행사할 권한이 없습니다."),
    CUSTODY_SERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CUSTODY-003", "Custody 데이터를 변환하는 중 오류가 발생했습니다."),
    CUSTODY_INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "CUSTODY-004", "유효하지 않은 초대 코드입니다."),
    CUSTODY_ALREADY_ERROR(HttpStatus.CONFLICT, "CUSTODY-005", "이미 해당 아기에 대한 양육권이 있습니다."),

    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "DIARY-001", "일기를 찾을 수 없습니다."),
    DIARY_ALREADY_EXISTS(HttpStatus.CONFLICT, "DIARY-002", "해당 날짜에 이미 작성된 다이어리가 존재합니다."),
    INVALID_DATE_REQUEST(HttpStatus.BAD_REQUEST, "DIARY-003", "미래 날짜는 조회할 수 없습니다."),

    INVALID_IMAGE_INPUT(HttpStatus.BAD_REQUEST, "IMAGE-001", "default image 와 create image 중 하나만 존재해야 합니다."),
    INVALID_EXISTING_IMAGE(HttpStatus.BAD_REQUEST, "IMAGE-002", "기존 이미지 url이 데이터베이스와 일치하지 않습니다."),

    PIYONG_NOT_FOUND(HttpStatus.NOT_FOUND, "PIYONG-001", "Piyong 를 찾을 수 없습니다."),
    PIYONG_SERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PIYONG-002", "Piyong 데이터를 변환하는 중 오류가 발생했습니다."),

    GROWTH_PERCENTILE_NOT_FOUND(HttpStatus.NOT_FOUND, "STATISTICS-001", "백분위수를 찾을 수 없습니다."),

    NOTIFICATION_INVALID_SCHEDULED_DATE(HttpStatus.BAD_REQUEST, "NOTIFICATION-001", "과거 날짜로 알림을 등록할 수 없습니다."),

    INVALID_JSON_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "JSON-001", "JSON 변환 중 오류가 발생했습니다."),

    VOICE_DATA_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "VOICE-RECOGNITION-001", "음성 인식 데이터 처리 중 오류가 발생했습니다."),

    DIARY_KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "DIARY-KEYWORD-001", "삭제할 키워드가 존재하지 않습니다."),
    DIARY_KEYWORD_UPDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "DIARY-KEYWORD-002", "수정할 키워드를 찾을 수 없습니다."),
    DIARY_KEYWORD_ALREADY_EXISTS(HttpStatus.CONFLICT, "DIARY-KEYWORD-003", "이미 존재하는 키워드명입니다."),
    DIARY_KEYWORD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "DIARY-KEYWORD-004", "키워드는 최대 6개까지만 등록할 수 있습니다."),

    USER_CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-CHATROOM-001", "해당하는 user chat room 이 존재하지 않습니다."),
    USER_CHATROOM_NOT_JOIN(HttpStatus.BAD_REQUEST, "USER-CHATROOM-002", "해당 유저는 채팅방에 참여하지 않았습니다."),

    CHATTING_NOT_FOUND(HttpStatus.NOT_FOUND, "CHATTING-001","해당하는 채팅이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    }
