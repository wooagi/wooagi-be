package com.agarang.domain.statistics.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.service.BabyService;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.ExcretionColor;
import com.agarang.domain.record.entity.enumeration.ExcretionStatus;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.repository.RecordRepository;
import com.agarang.domain.record.repository.type.ExcretionRepository;
import com.agarang.domain.statistics.dto.ActiveTime;
import com.agarang.domain.statistics.dto.ActiveTimeBlock;
import com.agarang.domain.statistics.dto.DailyActiveTime;
import com.agarang.domain.statistics.dto.response.ExcretionWeeklyGetResponse;
import com.agarang.domain.statistics.dto.response.WeeklyGetResponse;
import com.agarang.domain.statistics.service.WeeklyStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.statistics.service.impl<br>
 * fileName       : ExcretionWeeklyStatisticsService.java<br>
 * author         : nature1216 <br>
 * date           : 2/4/25<br>
 * description    : 배변 주간통계 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/4/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class ExcretionWeeklyStatisticsService implements WeeklyStatisticsService {
    private final BabyService babyService;
    private final RecordRepository recordRepository;
    private final ExcretionRepository excretionRepository;

    public static final Integer EXCRETION_BLOCK_FREQUENCY = 4;

    /**
     * 특정 아기의 배변 주간 통계를 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 아기의 배변 기록을 분석하여 평균 배변 횟수, 활동 시간 블록, 비정상적인 배변 횟수,
     * 배변 색상 및 상태 통계를 포함한 주간 통계를 반환합니다.
     * </p>
     *
     * @param date   기준 날짜
     * @param userId 사용자 ID
     * @param babyId 아기 ID
     * @return 배변 주간 통계를 포함한 {@link WeeklyGetResponse} 객체
     */
    @Override
    public WeeklyGetResponse getWeeklyStatistics(LocalDate date, Integer userId, Integer babyId) {
        Baby baby = babyService.getBabyById(babyId);
        List<DailyActiveTime> activeTimes = getActiveTimes(date, baby);
        List<ActiveTimeBlock> activeTimeBlocks = analyzePattern(date, baby);
        BigDecimal averageCount = getAverageCount(date, baby);
        Integer abnormalCount = getAbnormalCount(date, baby);
        Map<ExcretionColor, Integer> colorCount = getCountByExcretionColor(date, baby);
        Map<ExcretionStatus, Integer> statusCount = getCountByExcretionStatus(date, baby);

        return ExcretionWeeklyGetResponse.builder()
                .targetDate(date)
                .recordType(RecordType.EXCRETION)
                .averageCount(averageCount)
                .activeTimes(activeTimes)
                .activeTimeBlocks(activeTimeBlocks)
                .abnormalCount(abnormalCount)
                .colorCount(colorCount)
                .statusCount(statusCount)
                .build();
    }

    /**
     * 특정 아기의 주간 배변 활동 시간을 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 배변 활동 시간을 분석하여 반환합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 각 날짜별 배변 활동 시간을 포함한 {@link DailyActiveTime} 리스트
     */
    @Override
    public List<DailyActiveTime> getActiveTimes(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();
        List<Record> weeklyRecords = recordRepository
                .findStartedAtByBabyAndDateBetween(baby, start, date.atTime(LocalTime.MAX), RecordType.EXCRETION);

        Map<LocalDate, List<Record>> recordsByDate = weeklyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getStartedAt().toLocalDate()));

        return recordsByDate.entrySet().stream()
                .map(entry -> DailyActiveTime.builder()
                        .date(entry.getKey())
                        .dailyActiveTimes(
                                entry.getValue().stream()
                                        .map(record -> ActiveTime.builder()
                                                .start(record.getStartedAt().getHour())
                                                .end(record.getStartedAt().getHour() + 1)
                                                .build())
                                        .sorted(Comparator.comparing(ActiveTime::getStart))
                                        .collect(Collectors.toList())
                        )
                        .build())
                .sorted(Comparator.comparing(DailyActiveTime::getDate))
                .collect(Collectors.toList());
    }

    /**
     * 특정 아기의 주간 평균 배변 횟수를 계산합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 배변 기록을 분석하여 평균 횟수를 계산합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 7일간의 평균 배변 횟수
     */
    @Override
    public BigDecimal getAverageCount(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();

        int totalCount = recordRepository
                .getCountByRecordTypeAndBaby(baby, RecordType.EXCRETION, start, date.atTime(LocalTime.MAX));

        return BigDecimal.valueOf(totalCount)
                .divide(BigDecimal.valueOf(7), 1, RoundingMode.HALF_UP);
    }

    /**
     * 해당 통계의 기록 유형을 반환합니다.
     *
     * @return 배변 기록 유형 {@link RecordType#EXCRETION}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.EXCRETION;
    }

    /**
     * 특정 아기의 배변 패턴을 분석합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 배변 기록을 분석하여
     * 일정한 패턴이 있는 활동 시간 블록을 반환합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 분석된 활동 시간 블록 리스트 {@link ActiveTimeBlock}
     */
    @Override
    public List<ActiveTimeBlock> analyzePattern(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();
        List<LocalDateTime> records = recordRepository
                .findStartedAtByBabyAndDateBetween(baby, start, date.atTime(LocalTime.MAX), RecordType.EXCRETION).stream()
                .map(Record::getStartedAt)
                .toList();

        Map<ActiveTimeBlock, Integer> frequencyMap = extractActiveTimeBlocks(records);

        List<ActiveTimeBlock> result = filterByFrequency(new ArrayList<>(frequencyMap.keySet()), frequencyMap, EXCRETION_BLOCK_FREQUENCY);
        result.sort(Comparator.comparing(ActiveTimeBlock::getStart));

        return result;
    }

    /**
     * 주어진 배변 활동 시간을 기반으로 활동 시간 블록을 생성합니다.
     *
     * <p>
     * 30분 단위로 배변 활동을 그룹화하고 빈도를 계산하여 맵으로 반환합니다.
     * </p>
     *
     * @param data 배변 활동 시간 목록
     * @return 활동 시간 블록과 해당 빈도를 포함한 맵
     */
    private Map<ActiveTimeBlock, Integer> extractActiveTimeBlocks(List<LocalDateTime> data) {
        Map<ActiveTimeBlock, Integer> frequencyMap = new HashMap<>();

        for(LocalDateTime date: data) {
            LocalTime start = roundToNearestHalfHour(date.toLocalTime());
            LocalTime end = start.plusMinutes(30);
            ActiveTimeBlock block = new ActiveTimeBlock(start, end);

            frequencyMap.put(block, frequencyMap.getOrDefault(block, 0) + 1);
        }

        return frequencyMap;
    }

    /**
     * 주어진 시간을 가장 가까운 30분 단위로 반올림합니다.
     *
     * @param time 반올림할 시간
     * @return 30분 단위로 정렬된 시간
     */
    private LocalTime roundToNearestHalfHour(LocalTime time) {
        int minute = time.getMinute();
        int hour = time.getHour();

        if (minute < 30) {
            return LocalTime.of(hour, 0);
        } else {
            return LocalTime.of(hour, 30);
        }
    }

    /**
     * 일정 빈도 이상 발생한 활동 시간 블록만 필터링하여 반환합니다.
     *
     * <p>
     * 배변 활동 패턴에서 특정 빈도 이상 발생한 블록만 포함하도록 필터링합니다.
     * </p>
     *
     * @param blocks        활동 시간 블록 리스트
     * @param frequencyMap  활동 시간 블록별 빈도수 맵
     * @param minFrequency  최소 빈도 기준
     * @return 필터링된 활동 시간 블록 리스트
     */
    private List<ActiveTimeBlock> filterByFrequency(List<ActiveTimeBlock> blocks, Map<ActiveTimeBlock, Integer> frequencyMap, int minFrequency) {
        List<ActiveTimeBlock> filteredBlocks = new ArrayList<>();

        for (ActiveTimeBlock block : blocks) {
            if (frequencyMap.getOrDefault(block, 0) >= minFrequency) {
                filteredBlocks.add(block);
            }
        }

        return filteredBlocks;
    }

    /**
     * 특정 아기의 비정상적인 배변 횟수를 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 비정상적인 배변 기록 횟수를 조회합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 비정상적인 배변 횟수
     */
    private Integer getAbnormalCount(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();

        return excretionRepository.countAbnormalExcretionByDateAndBaby(baby, start, date.atTime(LocalTime.MAX));
    }

    /**
     * 특정 아기의 배변 상태별 발생 횟수를 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 배변 상태(예: 정상, 설사 등)별 빈도를 계산합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 배변 상태별 빈도를 포함한 맵 {@link ExcretionStatus, Integer}
     */
    private Map<ExcretionStatus, Integer> getCountByExcretionStatus(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();

        List<Object[]> statusCount = excretionRepository.countExcretionStatusByBabyDateBetween(baby, start, date.atTime(LocalTime.MAX));

        return statusCount.stream()
                .collect(Collectors.toMap(
                        result -> (ExcretionStatus) result[0],
                        result -> ((Long) result[1]).intValue()
                ));
    }

    /**
     * 특정 아기의 배변 색상별 발생 횟수를 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 배변 색상별 빈도를 계산합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 배변 색상별 빈도를 포함한 맵 {@link ExcretionColor, Integer}
     */
    private Map<ExcretionColor, Integer> getCountByExcretionColor(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();

        List<Object[]> colorCount = excretionRepository.countExcretionColorByBabyDateBetween(baby, start, date.atTime(LocalTime.MAX));

        return colorCount.stream()
                .collect(Collectors.toMap(
                        result -> (ExcretionColor) result[0],
                        result -> ((Long) result[1]).intValue()
                ));
    }
}
