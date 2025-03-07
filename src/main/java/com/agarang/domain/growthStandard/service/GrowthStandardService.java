package com.agarang.domain.growthStandard.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.baby.repository.BabyRepository;
import com.agarang.domain.growthStandard.dto.mapper.GrowthStandardMapper;
import com.agarang.domain.growthStandard.dto.response.DetailGrowthStandardResponse;
import com.agarang.domain.growthStandard.dto.response.GrowthStandardItemResponse;
import com.agarang.domain.growthStandard.dto.response.GrowthStandardResponse;
import com.agarang.domain.growthStandard.entity.GrowthStandard;
import com.agarang.domain.growthStandard.entity.GrowthStandardType;
import com.agarang.domain.growthStandard.repository.GrowthStandardRepository;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.growthStandard.service<br>
 * fileName       : GrowthStandardService.java<br>
 * author         : okeio<br>
 * date           : 25. 1. 24.<br>
 * description    : 성장 발달 서비스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.01.24          okeio           최초생성<br>
 * 25.02.05          okeio           calculateDayFromBirthOfBaby 메서드 수정<br>
 * 25.02.06          okeio           DB에서 가져온 데이터 null 체크<br>
 * <br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GrowthStandardService {
    private final BabyRepository babyRepository;
    private final GrowthStandardRepository growthStandardRepository;
    private final GrowthStandardMapper growthStandardMapper;


    /**
     * 아기 개월 수에 따른 SUMMARY 타입의 성장 발달 정보를 조회합니다.
     *
     * @param babyId 아기 id
     * @return 성장 발달 내용을 담은 객체{@link GrowthStandardResponse}
     */
    public GrowthStandardResponse getSummaryGrowthData(Integer babyId) {
        int dayFromBirth = calculateDayFromBirthOfBaby(babyId);

        List<GrowthStandard> list = growthStandardRepository.findByStartDayLessThanEqualAndEndDayGreaterThanEqualAndGrowthStandardType
                (dayFromBirth, dayFromBirth, GrowthStandardType.SUMMARY);

        if (list.isEmpty()) {
            return new GrowthStandardResponse(null);
        }

        return growthStandardMapper.mapToGrowthStandardResponse(list.get(0));
    }


    /**
     * 아기 개월 수에 따른 성장 발달 정보를 상세 조회합니다.
     *
     * @param babyId 아기 id
     * @return 여러 타입의 성장 발달 내용을 담은 객체{@link DetailGrowthStandardResponse}
     */
    public DetailGrowthStandardResponse getDetailGrowthData(Integer babyId) {
        int dayFromBirth = calculateDayFromBirthOfBaby(babyId);

        List<GrowthStandard> babyDatalist = growthStandardRepository.findByDayAndGrowthStandardTypes(
                dayFromBirth,
                List.of(GrowthStandardType.GROWTH.toString(),
                        GrowthStandardType.SLEEP.toString(),
                        GrowthStandardType.LACTATION.toString()));

        List<GrowthStandardItemResponse> babyData = Optional.ofNullable(babyDatalist)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(gs -> new GrowthStandardItemResponse(gs.getDescription(), gs.getGrowthStandardType().name()))
                        .toList())
                .orElse(Collections.emptyList());

        List<GrowthStandard> motherDataList = growthStandardRepository.findByStartDayLessThanEqualAndEndDayGreaterThanEqualAndGrowthStandardType
                (dayFromBirth, dayFromBirth, GrowthStandardType.MOTHER);

        List<GrowthStandard> fatherDataList = growthStandardRepository.findByStartDayLessThanEqualAndEndDayGreaterThanEqualAndGrowthStandardType
                (dayFromBirth, dayFromBirth, GrowthStandardType.FATHER);

        String motherDescription = motherDataList.isEmpty() ? null : motherDataList.get(0).getDescription();
        String fatherDescription = fatherDataList.isEmpty() ? null : fatherDataList.get(0).getDescription();

        return new DetailGrowthStandardResponse(babyData, motherDescription, fatherDescription);
    }

    /**
     * 현재 날짜를 기준으로 출생일로부터 며칠이 지났는지 계산합니다.
     *
     * @param babyId 아기 id
     * @return 생후 몇 일, 일령
     */
    public int calculateDayFromBirthOfBaby(int babyId) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(
                        () ->
                                new BusinessException(ErrorCode.BABY_NOT_FOUND)
                );

        return (int) ChronoUnit.DAYS.between(baby.getBirth().toLocalDate(), LocalDate.now());
    }
}
