package com.agarang.domain.statistics.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.service.BabyService;
import com.agarang.domain.custody.service.CustodyService;
import com.agarang.domain.record.entity.enumeration.GrowthStatusType;
import com.agarang.domain.record.entity.type.GrowthStatus;
import com.agarang.domain.record.repository.type.GrowthStatusRepository;
import com.agarang.domain.statistics.dto.GrowthRecord;
import com.agarang.domain.statistics.dto.response.GrowthAverageResponse;
import com.agarang.domain.statistics.dto.response.GrowthHistoryResponse;
import com.agarang.domain.statistics.dto.response.GrowthResponse;
import com.agarang.domain.statistics.dto.response.GrowthStatisticsResponse;
import com.agarang.domain.statistics.entity.GrowthPercentile;
import com.agarang.domain.statistics.repository.GrowthPercentileRepository;
import com.agarang.domain.user.entity.User;
import com.agarang.domain.user.service.UserService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

/**
 * packageName    : com.agarang.domain.statistics.service<br>
 * fileName       : StatisticsService.java<br>
 * author         : nature1216 <br>
 * date           : 2025-02-04<br>
 * description    : 성장곡선 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-02-04          nature1216          최초생성<br>
 * <br>
 */
@Service
@RequiredArgsConstructor
public class GrowthStatisticsService {
    private final BabyService babyService;
    private final UserService userService;
    private final CustodyService custodyService;
    private final GrowthStatusRepository growthStatusRepository;
    private final GrowthPercentileRepository growthPercentileRepository;

    /**
     * 특정 아기의 성장 상태 이력을 조회합니다.
     *
     * <p>
     * 아기의 성장 상태 유형에 따른 기록된 성장 이력과 평균 성장 데이터를 반환합니다.
     * </p>
     *
     * @param userId 사용자 ID
     * @param babyId 아기 ID
     * @param type   성장 상태 유형
     * @return 성장 이력을 포함한 {@link GrowthHistoryResponse} 객체
     */
    public GrowthHistoryResponse getGrowthHistory(Integer userId, Integer babyId, GrowthStatusType type) {
        Baby baby = babyService.getBabyById(babyId);
        User user = userService.findUserById(userId);
        Integer day = babyService.calculateDaysSinceBirth(babyId);

        custodyService.checkCustody(user, baby);

        List<GrowthResponse> histories = growthStatusRepository.findAllByGrowthStatusTypeAndBaby(type, baby);
        List<GrowthAverageResponse> averages = growthPercentileRepository.findGrowthAverageByDayAndSexAndType(day, baby.getSex(), type);


        return GrowthHistoryResponse.builder()
                .babyId(babyId)
                .growthStatusType(type)
                .histories(histories)
                .averages(averages)
                .build();
    }

    /**
     * 특정 성장 기록에 대한 통계를 조회합니다.
     *
     * <p>
     * 성장 기록을 기반으로 성장 변화량 및 성장 백분위 순위를 계산하여 반환합니다.
     * </p>
     *
     * @param userId   사용자 ID
     * @param recordId 성장 기록 ID
     * @return 성장 통계를 포함한 {@link GrowthStatisticsResponse} 객체
     */
    public GrowthStatisticsResponse getStatistics(Integer userId, Integer recordId) {
        User user = userService.findUserById(userId);
        GrowthStatus growthStatus = growthStatusRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));
        Baby baby = babyService.getBabyById(growthStatus.getRecord().getBaby().getBabyId());

        custodyService.checkCustody(user, baby);


        BigDecimal change = calculateChange(growthStatus, baby);
        Integer rank = calculateRank(baby, growthStatus);


        return GrowthStatisticsResponse.builder()
                .growthChange(change)
                .rank(rank)
                .build();

    }

    /**
     * 특정 성장 상태 기록의 2주 전 대비 성장 변화량을 계산합니다.
     *
     * <p>
     * 주어진 성장 상태 기록을 기준으로, 2주 전의 가장 최근 성장 기록과 비교하여
     * 성장 변화량을 반환합니다.
     * </p>
     *
     * @param growthStatus 현재 성장 상태 기록
     * @param baby         아기 객체
     * @return 성장 변화량
     */
    private BigDecimal calculateChange(GrowthStatus growthStatus, Baby baby) {
        LocalDate twoWeeksAgo = growthStatus.getRecord().getStartedAt().minusWeeks(2).toLocalDate();
        GrowthRecord record = growthStatusRepository
                        .findTopByChildIdAndStartedAtBeforeOrderByStartedAtDesc
                                (baby, twoWeeksAgo, growthStatus.getGrowthStatusType())
                        .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return growthStatus.getSize().subtract(record.getSize());
    }

    /**
     * 특정 성장 상태 기록의 백분위 순위를 계산합니다.
     *
     * <p>
     * 현재 측정된 성장 값을 백분위 데이터와 비교하여 아기의 성장 순위를 반환합니다.
     * </p>
     *
     * @param baby         아기 객체
     * @param growthStatus 현재 성장 상태 기록
     * @return 성장 백분위 순위 (1~99)
     */
    private Integer calculateRank(Baby baby, GrowthStatus growthStatus) {
        Integer day = babyService.calculateDaysSinceBirth(baby.getBabyId());
        GrowthPercentile percentile = growthPercentileRepository.findByDayAndSexAndType(day, baby.getSex(), growthStatus.getGrowthStatusType())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROWTH_PERCENTILE_NOT_FOUND));
        BigDecimal measuredValue = growthStatus.getSize();

        Map<Integer, BigDecimal> percentiles = percentile.getPercentileMap();
        List<Integer> sortedPercentiles = new ArrayList<>(percentiles.keySet());
        Collections.sort(sortedPercentiles);

        for(int i=0; i<sortedPercentiles.size()-1; i++) {
            int lowerPercentile = sortedPercentiles.get(i);
            int upperPercentile = sortedPercentiles.get(i+1);

            BigDecimal lowerValue = percentiles.get(lowerPercentile);
            BigDecimal upperValue = percentiles.get(upperPercentile);

            if(measuredValue.compareTo(lowerValue) >= 0
                    && measuredValue.compareTo(upperValue) <= 0) {
                BigDecimal range = upperValue.subtract(lowerValue);
                BigDecimal position = measuredValue.subtract(lowerValue);

                BigDecimal ratio = position.divide(range, 4, RoundingMode.HALF_UP);

                BigDecimal result = BigDecimal.valueOf(lowerPercentile)
                        .add(ratio.multiply(BigDecimal.valueOf(upperPercentile - lowerPercentile)));

                return result.setScale(0, RoundingMode.HALF_UP).intValue();
            }
        }

        if(measuredValue.compareTo(percentiles.get(sortedPercentiles.get(0))) < 0) return 1;
        if(measuredValue.compareTo(percentiles.get(sortedPercentiles.get(sortedPercentiles.size() - 1))) > 0) return 99;

        return 50;
    }
}
