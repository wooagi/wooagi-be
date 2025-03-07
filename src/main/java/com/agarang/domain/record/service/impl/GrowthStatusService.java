package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.event.BabyGrowthStatusRecordEvent;
import com.agarang.domain.record.dto.mapper.GrowthStatusMapper;
import com.agarang.domain.record.dto.request.GrowthStatusCreateRequest;
import com.agarang.domain.record.dto.request.GrowthStatusUpdateRequest;
import com.agarang.domain.record.dto.response.GrowthStatusGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.entity.type.GrowthStatus;
import com.agarang.domain.record.repository.type.GrowthStatusRepository;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : GrowthStatusService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 발육상태 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */

@Service
@RequiredArgsConstructor
@Transactional
public class GrowthStatusService implements RecordService<GrowthStatusCreateRequest, GrowthStatusGetResponse, GrowthStatusUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final GrowthStatusRepository growthStatusRepository;
    private final GrowthStatusMapper growthStatusMapper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 새로운 성장 상태 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 성장 상태 기록을 생성하고,
     * 해당 정보를 `GrowthStatus` 엔터티로 저장합니다.
     * 기록이 생성되면 성장 상태 이벤트를 발행합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 성장 상태 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, GrowthStatusCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);

        GrowthStatus growthStatus = GrowthStatus.builder()
                .record(record)
                .growthStatusType(request.getGrowthStatusType())
                .size(request.getSize())
                .build();

        growthStatusRepository.save(growthStatus);

        eventPublisher.publishEvent(new BabyGrowthStatusRecordEvent(this, babyId, growthStatus.getGrowthStatusType(), growthStatus.getSize()));

        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 성장 상태 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 성장 상태 기록을 조회하고,
     * 변환된 {@link GrowthStatusGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 성장 상태 기록 정보를 포함한 {@link GrowthStatusGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public GrowthStatusGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        GrowthStatus growthStatus = growthStatusRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return growthStatusMapper.mapToGetResponse(record, growthStatus);
    }

    /**
     * 특정 성장 상태 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 성장 상태 기록을 업데이트합니다.
     * 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * 기록이 수정되면 성장 상태 이벤트를 발행합니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  성장 상태 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, GrowthStatusUpdateRequest request, MultipartFile image) {
        GrowthStatus growthStatus = growthStatusRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        recordCommonService.updateRecord(userId, recordId, request, image);
        growthStatusMapper.updateGrowthStatusFromUpdateRequest(request, growthStatus);

        eventPublisher.publishEvent(new BabyGrowthStatusRecordEvent(this, growthStatus.getRecord().getBaby().getBabyId(), growthStatus.getGrowthStatusType(), growthStatus.getSize()));
    }

    /**
     * 특정 성장 상태 기록을 삭제합니다.
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
        GrowthStatus growthStatus = growthStatusRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        growthStatusRepository.delete(growthStatus);
        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 성장 상태 기록의 타입을 반환합니다.
     *
     * @return 성장 상태 기록 타입 {@link RecordType#GROWTH_STATUS}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.GROWTH_STATUS;
    }
}
