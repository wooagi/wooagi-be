package com.agarang.domain.statistics.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.service.BabyService;
import com.agarang.domain.custody.service.CustodyService;
import com.agarang.domain.record.dto.SleepHour;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.repository.RecordRepository;
import com.agarang.domain.record.repository.type.SleepRepository;
import com.agarang.domain.statistics.dto.ActiveTime;
import com.agarang.domain.statistics.dto.DailyActiveTime;
import com.agarang.domain.statistics.dto.SleepActiveTime;
import com.agarang.domain.statistics.dto.ActiveTimeBlock;
import com.agarang.domain.statistics.dto.response.SleepWeeklyGetResponse;
import com.agarang.domain.statistics.dto.response.WeeklyGetResponse;
import com.agarang.domain.statistics.service.WeeklyStatisticsService;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * packageName    : com.agarang.domain.statistics.service.impl<br>
 * fileName       : SleepWeeklyStatisticsService.java<br>
 * author         : nature1216 <br>
 * date           : 2/4/25<br>
 * description    : 수면 주간통계 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/4/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class SleepWeeklyStatisticsService implements WeeklyStatisticsService {
    private final BabyService babyService;
    private final UserService userService;
    private final CustodyService custodyService;
    private final SleepRepository sleepRepository;
    private final RecordRepository recordRepository;

    public static final Integer SLEEP_BLOCK_FREQUENCY = 4;

    /**
     * 특정 아기의 수면 주간 통계를 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 아기의 수면 기록을 분석하여 평균 수면 횟수, 활동 시간 블록,
     * 평균 수면 시간, 주간 변화량 등의 정보를 포함한 주간 통계를 반환합니다.
     * </p>
     *
     * @param date   기준 날짜
     * @param userId 사용자 ID
     * @param babyId 아기 ID
     * @return 수면 주간 통계를 포함한 {@link WeeklyGetResponse} 객체
     */
    @Override
    public WeeklyGetResponse getWeeklyStatistics(LocalDate date, Integer userId, Integer babyId) {
        Baby baby = babyService.getBabyById(babyId);
        User user = userService.findUserById(userId);
        custodyService.checkCustody(user, baby);

        BigDecimal averageCount = getAverageCount(date, baby);
        List<DailyActiveTime> activeTimes = getActiveTimes(date, baby);
        BigDecimal averageHours = getWeeklyAverageHours(date, baby);
        BigDecimal weeklyChange = calculateWeeklyChange(date, baby, averageHours);
        List<ActiveTimeBlock> activeTimeBlocks = analyzePattern(date, baby);

        return SleepWeeklyGetResponse.builder()
                .targetDate(date)
                .recordType(RecordType.SLEEP)
                .averageCount(averageCount)
                .activeTimes(activeTimes)
                .averageHour(averageHours)
                .activeTimeBlocks(activeTimeBlocks)
                .weeklyChange(weeklyChange)
                .build();
    }

    /**
     * 특정 아기의 주간 수면 활동 시간을 조회합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 수면 활동 시간을 분석하여 반환합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 각 날짜별 수면 활동 시간을 포함한 {@link DailyActiveTime} 리스트
     */
    @Override
    public List<DailyActiveTime> getActiveTimes(LocalDate date, Baby baby) {
        List<SleepHour> sleepRecords = sleepRepository
                .findSleepByBabyAndDateRange(baby, date.minusDays(6).atStartOfDay(), date.atTime(LocalTime.MAX));

        List<SleepActiveTime> sleepActiveTimes= sleepRecords.stream()
                .flatMap(record -> splitSleepIntoDailyActiveTimes(record.getStart(), record.getEnd()).stream())
                .toList();

        Map<LocalDate, List<ActiveTime>> groupedByDate = sleepActiveTimes.stream()
                .collect(Collectors.groupingBy(SleepActiveTime::getDate, Collectors.toList()));

        return groupedByDate.entrySet().stream()
                .map(entry -> DailyActiveTime.builder()
                        .date(entry.getKey())
                        .dailyActiveTimes(entry.getValue().stream()
                                        .sorted(Comparator.comparing(ActiveTime::getStart))
                                        .collect(Collectors.toList())
                        )
                        .build())
                .sorted(Comparator.comparing(DailyActiveTime::getDate))
                .toList();
    }

    /**
     * 특정 아기의 주간 평균 수면 횟수를 계산합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 수면 기록을 분석하여 평균 횟수를 계산합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 7일간의 평균 수면 횟수
     */
    @Override
    public BigDecimal getAverageCount(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();

        int totalCount = recordRepository
                .getCountByRecordTypeAndBaby(baby, RecordType.SLEEP, start, date.atTime(LocalTime.MAX));

        return BigDecimal.valueOf(totalCount)
                .divide(BigDecimal.valueOf(7), 1, RoundingMode.HALF_UP);
    }

    /**
     * 해당 통계의 기록 유형을 반환합니다.
     *
     * @return 수면 기록 유형 {@link RecordType#SLEEP}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.SLEEP;
    }

    /**
     * 특정 아기의 수면 패턴을 분석합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 수면 기록을 분석하여
     * 일정한 패턴이 있는 활동 시간 블록을 반환합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 분석된 활동 시간 블록 리스트 {@link ActiveTimeBlock}
     */
    public List<ActiveTimeBlock> analyzePattern(LocalDate date, Baby baby) {
        LocalDateTime start = date.minusDays(6).atStartOfDay();

        List<SleepHour> sleepHours = sleepRepository.findSleepByBabyAndDateRange(baby, start, date.atTime(LocalTime.MAX));

        Map<ActiveTimeBlock, Integer> frequencyMap = extractSleepBlocks(sleepHours);

        List<ActiveTimeBlock> filteredBlocks = filterByFrequency(new ArrayList<>(frequencyMap.keySet()), frequencyMap, SLEEP_BLOCK_FREQUENCY);

        List<ActiveTimeBlock> result = mergeActiveTimeBlocks(filteredBlocks);
        result.sort(Comparator.comparing(ActiveTimeBlock::getStart));

        return result;
    }

    /**
     * 주어진 수면 활동 시간을 기반으로 활동 시간 블록을 생성합니다.
     *
     * <p>
     * 30분 단위로 수면 활동을 그룹화하고 빈도를 계산하여 맵으로 반환합니다.
     * </p>
     *
     * @param sleepHours 수면 활동 시간 목록
     * @return 활동 시간 블록과 해당 빈도를 포함한 맵
     */
    private Map<ActiveTimeBlock, Integer> extractSleepBlocks(List<SleepHour> sleepHours) {
        Map<ActiveTimeBlock, Integer> frequencyMap = new HashMap<>();

        for (SleepHour sleepHour : sleepHours) {
            LocalTime start = adjustToHalfHourSlot(sleepHour.getStart().toLocalTime());
            LocalTime end = adjustToHalfHourSlot(sleepHour.getEnd().toLocalTime());

            while (!start.equals(end)) {
                ActiveTimeBlock block = new ActiveTimeBlock(start, start.plusMinutes(30));
                frequencyMap.put(block, frequencyMap.getOrDefault(block, 0) + 1);
                start = start.plusMinutes(30);
            }
        }

        return frequencyMap;
    }

    /**
     * 서로 연결된 활동 시간 블록을 병합합니다.
     *
     * <p>
     * 연속된 수면 블록을 하나의 블록으로 병합하여 리스트로 반환합니다.
     * </p>
     *
     * @param blocks 병합할 활동 시간 블록 리스트
     * @return 병합된 활동 시간 블록 리스트
     */
    private List<ActiveTimeBlock> mergeActiveTimeBlocks(List<ActiveTimeBlock> blocks) {
        LinkedList<ActiveTimeBlock> result = new LinkedList<>();
        ActiveTimeBlock prev = null;

        List<ActiveTimeBlock> sortedBlocks = new ArrayList<>(blocks);
        sortedBlocks.sort(Comparator.comparing(ActiveTimeBlock::getStart));

        for (ActiveTimeBlock block : sortedBlocks) {
            if (prev == null) {
                prev = block;
            } else if (prev.isConnected(block)) {
                prev = prev.merge(block);
            } else {
                result.add(prev);
                prev = block;
            }
        }

        if (prev != null) {
            result.add(prev);
        }

        return result;
    }

    /**
     * 주어진 시간을 가장 가까운 30분 단위로 반올림합니다.
     *
     * @param time 반올림할 시간
     * @return 30분 단위로 정렬된 시간
     */
    private LocalTime adjustToHalfHourSlot(LocalTime time) {
        int minute = time.getMinute();
        int hour = time.getHour();

        if (minute < 15) {
            return LocalTime.of(hour, 0);
        } else if (minute < 45) {
            return LocalTime.of(hour, 30);
        } else {
            return (hour == 23) ? LocalTime.of(0, 0) : LocalTime.of(hour + 1, 0);
        }
    }

    /**
     * 일정 빈도 이상 발생한 활동 시간 블록만 필터링하여 반환합니다.
     *
     * <p>
     * 수면 활동 패턴에서 특정 빈도 이상 발생한 블록만 포함하도록 필터링합니다.
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
     * 특정 아기의 주간 평균 수면 시간을 계산합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 수면 기록을 분석하여 평균 수면 시간을 계산합니다.
     * </p>
     *
     * @param date 기준 날짜
     * @param baby 아기 객체
     * @return 7일간의 평균 수면 시간
     */
    private BigDecimal getWeeklyAverageHours(LocalDate date, Baby baby) {
        List<SleepHour> sleepRecords = sleepRepository
                .findSleepByBabyAndDateRange(baby, date.minusDays(6).atStartOfDay(), date.atTime(LocalTime.MAX));

        if(sleepRecords.isEmpty()) return null;

        long totalSleepMinutes = sleepRecords.stream()
                .mapToLong(record -> Duration.between(record.getStart(), record.getEnd()).toMinutes())
                .sum();

        return BigDecimal.valueOf(totalSleepMinutes)
                .divide(BigDecimal.valueOf(7 * 60), 1, RoundingMode.HALF_UP);
    }

    /**
     * 수면 기록을 일자별 활동 시간 블록으로 변환합니다.
     *
     * <p>
     * 수면 시작 시간과 종료 시간을 기반으로 하루 단위로 분할하여 반환합니다.
     * </p>
     *
     * @param start 수면 시작 시간
     * @param end   수면 종료 시간
     * @return 일자별 수면 활동 시간 리스트
     */
    private List<SleepActiveTime> splitSleepIntoDailyActiveTimes(LocalDateTime start, LocalDateTime end) {
        List<SleepActiveTime> activeTimes = new ArrayList<>();
        LocalDate current = start.toLocalDate();

        while (!current.isAfter(end.toLocalDate())) {
            int startHour = current.isEqual(start.toLocalDate()) ? roundToNearestHour(start) : 0;
            int endHour = current.isEqual(end.toLocalDate()) ? roundToNearestHour(end) : 24;

            if(startHour < endHour) {
                activeTimes.add(SleepActiveTime.builder()
                        .date(current)
                        .start(startHour)
                        .end(endHour)
                        .build());
            }

            current = current.plusDays(1);
        }

        return activeTimes;
    }

    /**
     * 주어진 시간을 가장 가까운 정각 단위로 반올림합니다.
     *
     * @param time 반올림할 시간
     * @return 정각 단위로 변환된 시간
     */
    private Integer roundToNearestHour(LocalDateTime time) {
        int hour = time.getHour();
        return time.getMinute() >= 30 ? hour + 1 : hour;
    }

    /**
     * 특정 아기의 주간 평균 수면 시간 변화를 계산합니다.
     *
     * <p>
     * 이전 주와 현재 주의 평균 수면 시간을 비교하여 주간 변화량을 반환합니다.
     * </p>
     *
     * @param date    기준 날짜
     * @param baby    아기 객체
     * @param current 현재 주의 평균 수면 시간
     * @return 주간 평균 수면 시간 변화량
     */
    private BigDecimal calculateWeeklyChange(LocalDate date, Baby baby, BigDecimal current) {
        BigDecimal previous = getWeeklyAverageHours(date.minusDays(7), baby);

        if(Objects.isNull(current) || Objects.isNull(previous)) return null;

        if(previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return current.subtract(previous);
    }
}
