package com.agarang.domain.record.dto;

import com.agarang.domain.record.entity.enumeration.AntipyreticType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : com.agarang.domain.record.dto<br>
 * fileName       : AntipyreticInfo.java<br>
 * author         : nature1216 <br>
 * date           : 2/9/25<br>
 * description    : 해열제 기록 정보 DTO입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/9/25          nature1216          최초생성<br>
 */
public record AntipyreticInfo (
        Integer recordId,
        Integer babyId,
        AntipyreticType antipyreticType,
        BigDecimal amount,
        LocalDateTime startedAt
) {}
