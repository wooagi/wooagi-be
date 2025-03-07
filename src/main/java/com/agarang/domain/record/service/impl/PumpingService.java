package com.agarang.domain.record.service.impl;

import com.agarang.domain.record.dto.mapper.PumpingMapper;
import com.agarang.domain.record.dto.request.PumpingCreateRequest;
import com.agarang.domain.record.dto.request.PumpingUpdateRequest;
import com.agarang.domain.record.dto.response.PumpingGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.entity.type.Pumping;
import com.agarang.domain.record.repository.type.PumpingRepository;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : PumpingService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 유축 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PumpingService implements RecordService<PumpingCreateRequest, PumpingGetResponse, PumpingUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final PumpingRepository pumpingRepository;
    private final PumpingMapper pumpingMapper;

    private static final Integer PUMPING_DEFAULT_AMOUNT = 0;

    /**
     * 새로운 유축 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 유축 기록을 생성하고,
     * 최근 1개월간의 유축 기록을 조회하여 기본 수유량을 결정한 후 저장합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 유축 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, PumpingCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(1);
        Optional<Pumping> latestRecordOpt = pumpingRepository.getLatestPumpingByBabyAndStartedAtBetween(record.getBaby(), start, end);

        Integer rightAmount = determineAmount(latestRecordOpt.map(Pumping::getRightAmount), request.getRightAmount());
        Integer leftAmount = determineAmount(latestRecordOpt.map(Pumping::getLeftAmount), request.getLeftAmount());
        Integer totalAmount = determineAmount(latestRecordOpt.map(Pumping::getTotalAmount), request.getTotalAmount());

        Pumping pumping = Pumping.builder()
                .record(record)
                .position(request.getPosition())
                .rightAmount(rightAmount)
                .leftAmount(leftAmount)
                .totalAmount(totalAmount)
                .build();

        pumpingRepository.save(pumping);

        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 유축 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 유축 기록을 조회하고,
     * 변환된 {@link PumpingGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 유축 기록 정보를 포함한 {@link PumpingGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public PumpingGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        Pumping pumping = pumpingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return pumpingMapper.mapToGetResponse(record, pumping);
    }

    /**
     * 특정 유축 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 유축 기록을 업데이트합니다.
     * 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  유축 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, PumpingUpdateRequest request, MultipartFile image) {
        Pumping pumping = pumpingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        recordCommonService.updateRecord(userId, recordId, request, image);
        pumpingMapper.updatePumpingFromUpdateRequest(request, pumping);
    }

    /**
     * 특정 유축 기록을 삭제합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록을 삭제합니다.
     * </p>
     *
     * @param userId   기록을 삭제하는 사용자 ID
     * @param recordId 삭제할 기록의 ID
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void deleteRecord(Integer userId, Integer recordId) {
        Pumping pumping = pumpingRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        pumpingRepository.delete(pumping);
        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 유축 기록의 타입을 반환합니다.
     *
     * @return 유축 기록 타입 {@link RecordType#PUMPING}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.PUMPING;
    }

    /**
     * 유축량을 결정합니다.
     *
     * <p>
     * 요청된 유축량이 존재하면 해당 값을 반환하고, 요청 값이 없을 경우
     * 최근 1개월간의 데이터를 기준으로 최신 유축량을 반환합니다.
     * </p>
     *
     * @param latestValueOpt 최근 유축량 (Optional 값)
     * @param requestValue   요청된 유축량 (nullable)
     * @return 최종 결정된 유축량
     */
    private Integer determineAmount(Optional<Integer> latestValueOpt, Integer requestValue) {
        return (requestValue != null) ? requestValue : latestValueOpt.orElse(PUMPING_DEFAULT_AMOUNT);
    }
}
