package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.request.FeedingCreateRequest;
import com.agarang.domain.record.dto.request.FeedingUpdateRequest;
import com.agarang.domain.record.dto.response.FeedingGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.FeedingType;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.entity.type.Feeding;
import com.agarang.domain.record.repository.FeedingRepository;
import com.agarang.domain.record.service.FeedingTypeService;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : FeedingService.java<br>
 * author         : nature1216 <br>
 * date           : 1/29/25<br>
 * description    : 수유 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/29/25          nature1216          최초생성<br>
 */

@Service
@Transactional
public class FeedingService implements RecordService<FeedingCreateRequest, FeedingGetResponse, FeedingUpdateRequest> {
    private final Map<FeedingType, FeedingTypeService> feedingTypeServices;
    private final RecordCommonService recordCommonService;
    private final FeedingRepository feedingRepository;

    /**
     * FeedingService 생성자.
     *
     * <p>
     * 주어진 수유 타입 서비스 목록을 매핑하고, 공통 기록 서비스 및 수유 기록 저장소를 초기화합니다.
     * </p>
     *
     * @param feedingTypeServiceList 수유 타입별 서비스 목록
     * @param recordCommonService    공통 기록 관리 서비스
     * @param feedingRepository      수유 기록 저장소
     */
    public FeedingService(List<FeedingTypeService> feedingTypeServiceList,
                          RecordCommonService recordCommonService,
                          FeedingRepository feedingRepository) {
        this.feedingTypeServices = feedingTypeServiceList.stream()
                .collect(Collectors.toMap(FeedingTypeService::getFeedingType, service -> service));
        this.recordCommonService = recordCommonService;
        this.feedingRepository = feedingRepository;
    }

    /**
     * 새로운 수유 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 수유 기록을 생성하고,
     * 해당 기록을 저장한 후 적절한 수유 타입 서비스에서 추가적인 수유 기록을 생성합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 수유 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, FeedingCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);

        Feeding feeding = Feeding.builder()
                .record(record)
                .feedingType(request.getFeedingType())
                .build();

        feedingRepository.save(feeding);
        getService(feeding.getFeedingType()).createFeedingRecord(feeding, request);

        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 수유 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 수유 기록을 조회하고,
     * 적절한 수유 타입 서비스에서 변환된 응답 객체를 반환합니다.
     * </p>
     *
     * @param userId   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 수유 기록 정보를 포함한 {@link FeedingGetResponse} 객체
     */
    @Override
    public FeedingGetResponse getRecord(Integer userId, Integer recordId) {
        Record record = recordCommonService.getRecord(userId, recordId);
        Feeding feeding = getFeeding(recordId);

        return getService(feeding.getFeedingType()).getFeedingRecord(record, feeding);
    }

    /**
     * 특정 수유 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 수유 기록을 업데이트합니다.
     * 수유 타입이 변경된 경우 기존 기록을 삭제하고 새로운 기록을 생성합니다.
     * 변경이 없는 경우 기존 기록을 업데이트합니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  수유 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, FeedingUpdateRequest request, MultipartFile image) {
        request.validate();
        recordCommonService.updateRecord(userId, recordId, request, image);

        Feeding feeding = getFeeding(recordId);

        FeedingType currentType = feeding.getFeedingType();
        FeedingType newType = request.getFeedingType();

        if(!currentType.equals(newType)) { // 수유 타입 변경 시
            getService(currentType).deleteFeedingRecord(recordId);

            feeding.setFeedingType(newType);
            feedingRepository.save(feeding);

            FeedingCreateRequest createRequest;

            if(newType == FeedingType.BREAST_FEEDING) {
                createRequest = FeedingCreateRequest.builder()
                        .feedingType(FeedingType.BREAST_FEEDING)
                        .position(request.getPosition())
                        .leftTime(request.getLeftTime())
                        .rightTime(request.getRightTime())
                        .totalTime(request.getTotalTime())
                        .build();
            } else {
                createRequest = FeedingCreateRequest.builder()
                        .feedingType(newType)
                        .amount(request.getAmount())
                        .build();
            }

            getService(newType).createFeedingRecord(feeding, createRequest);
        } else { // 변경 없는 경우
            getService(currentType).updateFeedingRecord(recordId, request);
        }
    }

    /**
     * 특정 수유 기록을 삭제합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록을 삭제합니다.
     * 수유 타입별 서비스에서 개별적인 수유 기록을 먼저 삭제한 후, 전체 기록을 삭제합니다.
     * </p>
     *
     * @param userId   기록을 삭제하는 사용자 ID
     * @param recordId 삭제할 기록의 ID
     */
    @Override
    public void deleteRecord(Integer userId, Integer recordId) {
        Feeding feeding = getFeeding(recordId);

        getService(feeding.getFeedingType()).deleteFeedingRecord(recordId);
        feedingRepository.delete(feeding);
        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 수유 기록의 타입을 반환합니다.
     *
     * @return 수유 기록 타입 {@link RecordType#FEEDING}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.FEEDING;
    }

    /**
     * 특정 아기의 주간 평균 수유량을 계산합니다.
     *
     * <p>
     * 주어진 날짜를 기준으로 최근 7일간의 총 수유량을 계산하고,
     * 일주일 평균을 반환합니다.
     * </p>
     *
     * @param baby 조회할 아기 정보
     * @param date 기준 날짜
     * @return 주간 평균 수유량
     */
    public Integer getWeeklyAverageAmount(Baby baby, LocalDate date) {
        int totalWeeklyAmount = 0;

        for(int i=0;i<7;i++) {
            LocalDate currentDate = date.minusDays(i);

            int dailyTotalAmount = feedingTypeServices.values().stream()
                    .map(service -> service.getDailyTotalAmount(baby, currentDate))
                    .filter(amount -> amount != null && amount > 0)
                    .mapToInt(Integer::intValue)
                    .sum();

            totalWeeklyAmount += dailyTotalAmount;
        }

        return (int) Math.round((double) totalWeeklyAmount / 7);
    }

    /**
     * 주어진 수유 타입에 해당하는 서비스를 반환합니다.
     *
     * @param feedingType 조회할 수유 타입
     * @param <R>         반환할 수유 조회 응답 타입
     * @param <Q>         반환할 수유 업데이트 요청 타입
     * @return 해당 수유 타입의 {@link FeedingTypeService} 객체
     * @throws BusinessException 지원되지 않는 기록 타입일 경우 발생
     */
    private <R extends FeedingGetResponse, Q extends FeedingUpdateRequest> FeedingTypeService<R, Q> getService(FeedingType feedingType) {
        FeedingTypeService<R, Q> service = feedingTypeServices.get(feedingType);
        if(service == null) {
            throw new BusinessException(ErrorCode.UNSUPPORTED_RECORD_TYPE);
        }
        return service;
    }

    /**
     * 주어진 기록 ID에 해당하는 수유 기록을 조회합니다.
     *
     * @param recordId 조회할 기록의 ID
     * @return 조회된 {@link Feeding} 엔터티
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    private Feeding getFeeding(Integer recordId) {
        return feedingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));
    }
}
