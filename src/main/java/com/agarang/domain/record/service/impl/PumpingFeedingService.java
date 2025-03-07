package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.mapper.PumpingFeedingMapper;
import com.agarang.domain.record.dto.request.FeedingCreateRequest;
import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.PumpingFeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.FeedingType;
import com.agarang.domain.record.entity.type.Feeding;
import com.agarang.domain.record.entity.type.PumpingFeeding;
import com.agarang.domain.record.repository.type.PumpingFeedingRepository;
import com.agarang.domain.record.service.FeedingTypeService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : PumpingFeedingService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 유축수유 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class PumpingFeedingService implements FeedingTypeService<PumpingFeedingGetResponse, FeedingUpdateRequest> {
    private final PumpingFeedingRepository pumpingFeedingRepository;
    private final PumpingFeedingMapper pumpingFeedingMapper;

    private static final int PUMPING_FEEDING_DEFAULT_AMOUNT = 0;

    /**
     * 유축 수유 기록을 생성합니다.
     *
     * <p>
     * 주어진 수유 정보(`Feeding`)와 요청 데이터를 기반으로 유축 수유 기록을 생성하고 저장합니다.
     * 수유량을 결정하여 `PumpingFeeding` 엔터티에 저장합니다.
     * </p>
     *
     * @param feeding 수유 기록과 연관된 기본 `Feeding` 엔터티
     * @param request 유축 수유 기록 생성 요청 객체
     */
    @Override
    public void createFeedingRecord(Feeding feeding, FeedingCreateRequest request) {
        int amount = determineAmount(feeding, request.getAmount());

        PumpingFeeding pumpingFeeding = PumpingFeeding.builder()
                .feeding(feeding)
                .amount(amount)
                .build();

        pumpingFeedingRepository.save(pumpingFeeding);
    }

    /**
     * 특정 유축 수유 기록을 조회합니다.
     *
     * <p>
     * 주어진 기록 정보(`Record`)와 수유 정보(`Feeding`)를 기반으로
     * `PumpingFeeding` 엔터티를 조회하고, 변환된 응답 객체를 반환합니다.
     * </p>
     *
     * @param record  조회할 기록 정보
     * @param feeding 조회할 수유 정보
     * @return 조회된 유축 수유 기록 정보를 포함한 {@link PumpingFeedingGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public PumpingFeedingGetResponse getFeedingRecord(Record record, Feeding feeding) {
        PumpingFeeding pumpingFeeding = pumpingFeedingRepository.findById(record.getRecordId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return pumpingFeedingMapper.mapToGetResponse(record, feeding, pumpingFeeding);
    }

    /**
     * 특정 유축 수유 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 기록 ID를 기반으로 `PumpingFeeding` 엔터티를 조회한 후,
     * 요청 데이터를 반영하여 업데이트합니다.
     * </p>
     *
     * @param recordId 수정할 수유 기록의 ID
     * @param request  수유 기록 수정 요청 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateFeedingRecord(Integer recordId, FeedingUpdateRequest request) {
        PumpingFeeding pumpingFeeding = pumpingFeedingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        pumpingFeedingMapper.updatePumpingFeedingUpdateRequest(request, pumpingFeeding);
    }

    /**
     * 특정 유축 수유 기록을 삭제합니다.
     *
     * <p>
     * 주어진 기록 ID를 기반으로 `PumpingFeeding` 엔터티를 조회한 후, 해당 기록을 삭제합니다.
     * </p>
     *
     * @param recordId 삭제할 수유 기록의 ID
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void deleteFeedingRecord(Integer recordId) {
        PumpingFeeding pumpingFeeding = pumpingFeedingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        pumpingFeedingRepository.delete(pumpingFeeding);
    }

    /**
     * 유축 수유 유형을 반환합니다.
     *
     * @return 유축 수유 유형 {@link FeedingType#PUMPING_FEEDING}
     */
    @Override
    public FeedingType getFeedingType() {
        return FeedingType.PUMPING_FEEDING;
    }

    /**
     * 특정 날짜의 총 유축 수유량을 반환합니다.
     *
     * <p>
     * 주어진 아기와 날짜를 기준으로 평균 수유량을 계산하여 반환합니다.
     * </p>
     *
     * @param baby 조회할 아기 정보
     * @param date 조회할 날짜
     * @return 해당 날짜의 총 수유량
     */
    @Override
    public Integer getDailyTotalAmount(Baby baby, LocalDate date) {
        Double average = pumpingFeedingRepository
                .findAverageByBabyAndDate(baby, date);

        return Math.toIntExact(Math.round(average));
    }

    /**
     * 유축 수유량을 결정합니다.
     *
     * <p>
     * 요청된 수유량이 존재하면 해당 값을 반환하고, 요청 값이 없을 경우
     * 최근 1개월간의 데이터를 기준으로 최신 수유량을 반환합니다.
     * </p>
     *
     * @param feeding      수유 기록과 연관된 `Feeding` 엔터티
     * @param requestValue 요청된 수유량 (nullable)
     * @return 최종 결정된 수유량
     */
    private Integer determineAmount(Feeding feeding, Integer requestValue) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(1);

        Integer latestValue = pumpingFeedingRepository
                .getLatestPumpingFeedingByBabyAndStartedAtBetween(feeding.getRecord().getBaby(), start, end)
                .map(PumpingFeeding::getAmount)
                .orElse(PUMPING_FEEDING_DEFAULT_AMOUNT);

        return (requestValue != null) ? requestValue : latestValue;
    }
}
