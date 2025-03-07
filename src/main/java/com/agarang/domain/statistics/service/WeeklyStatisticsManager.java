package com.agarang.domain.statistics.service;

import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.service.FeedingTypeService;
import com.agarang.domain.statistics.dto.response.WeeklyGetResponse;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.statistics.service<br>
 * fileName       : WeeklyStatisticsManager.java<br>
 * author         : nature1216 <br>
 * date           : 2/4/25<br>
 * description    : 주간통계별 service 객체를 매핑하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/4/25          nature1216          최초생성<br>
 */
@Service
public class WeeklyStatisticsManager {
    private final Map<RecordType, WeeklyStatisticsService> statisticsServiceMap;

    /**
     * 주간 통계 서비스 매니저를 초기화합니다.
     *
     * <p>
     * 제공된 주간 통계 서비스 목록을 매핑하여 각 기록 유형별로 관리할 수 있도록 설정합니다.
     * </p>
     *
     * @param weeklyStatisticsServices 주간 통계 서비스를 구현한 서비스 목록
     */
    public WeeklyStatisticsManager(List<WeeklyStatisticsService> weeklyStatisticsServices) {
        this.statisticsServiceMap = weeklyStatisticsServices.stream()
                .collect(Collectors.toMap(WeeklyStatisticsService::getRecordType, service -> service));
    }

    /**
     * 특정 기록 유형에 대한 주간 통계를 조회합니다.
     *
     * <p>
     * 요청된 기록 유형에 맞는 주간 통계 서비스를 찾아 해당 통계를 반환합니다.
     * 지원되지 않는 기록 유형인 경우 예외가 발생합니다.
     * </p>
     *
     * @param recordType 기록 유형
     * @param date       기준 날짜
     * @param userId     사용자 ID
     * @param babyId     아기 ID
     * @return 주간 통계를 포함한 {@link WeeklyGetResponse} 객체
     * @throws BusinessException 지원되지 않는 기록 유형인 경우 예외 발생
     */
    public WeeklyGetResponse getWeeklyStatistics(RecordType recordType, LocalDate date, Integer userId, Integer babyId) {
        WeeklyStatisticsService service = statisticsServiceMap.get(recordType);

        if(service == null) {
            throw new BusinessException(ErrorCode.RECORD_TYPE_NOT_SUPPORTED);
        }

        return service.getWeeklyStatistics(date, userId, babyId);
    }
}
