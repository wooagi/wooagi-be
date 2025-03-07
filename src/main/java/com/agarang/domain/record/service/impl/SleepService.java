package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.mapper.SleepMapper;
import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.dto.request.SleepCreateRequest;
import com.agarang.domain.record.dto.request.SleepUpdateRequest;
import com.agarang.domain.record.dto.response.BaseRecordGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.entity.type.Sleep;
import com.agarang.domain.record.repository.type.SleepRepository;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : SleepService.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-23<br>
 * description    : 수면 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-23          nature1216          최초생성<br>
 * <br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SleepService implements RecordService<SleepCreateRequest, BaseRecordGetResponse, SleepUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final SleepRepository sleepRepository;
    private final SleepMapper sleepMapper;

    /**
     * 새로운 수면 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 수면 기록을 생성하고,
     * 종료 시간을 포함한 `Sleep` 엔터티를 저장합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 수면 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, SleepCreateRequest request) {

        Record record = recordCommonService.createRecord(userId, babyId, request);
        System.out.println(request.getEndedAt());

        Sleep sleep = Sleep.builder()
                .record(record)
                .endedAt(request.getEndedAt())
                .build();

        sleepRepository.save(sleep);

        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 수면 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 수면 기록을 조회하고,
     * 변환된 {@link BaseRecordGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 수면 기록 정보를 포함한 {@link BaseRecordGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public BaseRecordGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        Sleep sleep = sleepRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return sleepMapper.mapToGetResponse(record, sleep);
    }

    /**
     * 특정 수면 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 수면 기록을 업데이트합니다.
     * 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  수면 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     * @throws EntityNotFoundException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, SleepUpdateRequest request, MultipartFile image) {
        Sleep sleep = sleepRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException(("Sleep not found")));

        recordCommonService.updateRecord(userId, recordId, request, image);
        sleepMapper.updateSleepFromUpdateSleepRequest(request, sleep);
    }

    /**
     * 특정 수면 기록을 삭제합니다.
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
        Sleep sleep = sleepRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        sleepRepository.delete(sleep);

        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 수면 기록의 타입을 반환합니다.
     *
     * @return 수면 기록 타입 {@link RecordType#SLEEP}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.SLEEP;
    }
}
