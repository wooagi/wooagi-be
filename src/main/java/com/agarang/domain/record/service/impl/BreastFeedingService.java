package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.mapper.BreastFeedingMapper;
import com.agarang.domain.record.dto.request.FeedingCreateRequest;
import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.BreastFeedingGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.FeedingType;
import com.agarang.domain.record.entity.type.BreastFeeding;
import com.agarang.domain.record.entity.type.Feeding;
import com.agarang.domain.record.repository.type.BreastFeedingRepository;
import com.agarang.domain.record.service.FeedingTypeService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : BreastFeedingService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 모유수유 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class BreastFeedingService implements FeedingTypeService<BreastFeedingGetResponse, FeedingUpdateRequest> {
    private final BreastFeedingRepository breastFeedingRepository;
    private final BreastFeedingMapper breastFeedingMapper;

    /**
     * 모유 수유 기록을 생성합니다.
     *
     * <p>
     * 주어진 수유 정보(`Feeding`)와 요청 데이터를 기반으로
     * 새로운 `BreastFeeding` 엔터티를 생성하고 저장합니다.
     * </p>
     *
     * @param feeding 수유 기록과 연관된 기본 `Feeding` 엔터티
     * @param request 모유 수유 기록 생성 요청 객체
     */
    @Override
    public void createFeedingRecord(Feeding feeding, FeedingCreateRequest request) {
        BreastFeeding breastFeeding = BreastFeeding.builder()
                .feeding(feeding)
                .position(request.getPosition())
                .leftTime(request.getLeftTime())
                .rightTime(request.getRightTime())
                .totalTime(request.getTotalTime())
                .build();

        breastFeedingRepository.save(breastFeeding);
    }

    /**
     * 특정 모유 수유 기록을 조회합니다.
     *
     * <p>
     * 주어진 기록 정보(`Record`)와 수유 정보(`Feeding`)를 기반으로
     * `BreastFeeding` 엔터티를 조회하고, 변환된 응답 객체를 반환합니다.
     * </p>
     *
     * @param record  조회할 기록 정보
     * @param feeding 조회할 수유 정보
     * @return 조회된 모유 수유 기록 정보를 포함한 {@link BreastFeedingGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public BreastFeedingGetResponse getFeedingRecord(Record record, Feeding feeding) {
        BreastFeeding breastFeeding = breastFeedingRepository.findById(record.getRecordId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return breastFeedingMapper.mapToGetResponse(record, feeding, breastFeeding);
    }

    /**
     * 특정 모유 수유 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 기록 ID를 기반으로 `BreastFeeding` 엔터티를 조회한 후,
     * 요청 데이터를 반영하여 업데이트합니다.
     * </p>
     *
     * @param recordId 수정할 수유 기록의 ID
     * @param request  수유 기록 수정 요청 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateFeedingRecord(Integer recordId, FeedingUpdateRequest request) {
        BreastFeeding breastFeeding = breastFeedingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        breastFeedingMapper.updateBreastFeedingFromUpdateRequest(request, breastFeeding);
    }

    /**
     * 특정 모유 수유 기록을 삭제합니다.
     *
     * <p>
     * 주어진 기록 ID를 기반으로 `BreastFeeding` 엔터티를 조회한 후,
     * 해당 기록을 삭제합니다.
     * </p>
     *
     * @param recordId 삭제할 수유 기록의 ID
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void deleteFeedingRecord(Integer recordId) {
        BreastFeeding breastFeeding = breastFeedingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        breastFeedingRepository.delete(breastFeeding);
    }

    /**
     * 모유 수유 유형을 반환합니다.
     *
     * @return 모유 수유 유형 {@link FeedingType#BREAST_FEEDING}
     */
    @Override
    public FeedingType getFeedingType() {
        return FeedingType.BREAST_FEEDING;
    }

    /**
     * 특정 날짜의 총 모유 수유량을 반환합니다.
     *
     * @param baby 조회할 아기 정보
     * @param date 조회할 날짜
     * @return 해당 날짜의 총 수유량 (현재 0 반환)
     */
    @Override
    public Integer getDailyTotalAmount(Baby baby, LocalDate date) {
        return 0;
    }
}
