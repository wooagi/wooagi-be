package com.agarang.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.agarang.global.exception<br>
 * fileName       : BusinessException.java<br>
 * author         : okeio<br>
 * date           : 2025-01-23<br>
 * description    : Business 로직에서 발생하는 Exception 처리를 위한 커스텀 Exception 객체입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-23          okeio           최초생성<br>
 * <br>
 */
@AllArgsConstructor
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
}
