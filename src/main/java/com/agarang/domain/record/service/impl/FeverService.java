package com.agarang.domain.record.service.impl;

import com.agarang.domain.record.dto.mapper.FeverMapper;
import com.agarang.domain.record.dto.request.FeverCreateRequest;
import com.agarang.domain.record.dto.request.FeverUpdateRequest;
import com.agarang.domain.record.dto.response.FeverGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.entity.type.Fever;
import com.agarang.domain.record.repository.type.FeverRepository;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : FeverService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 열 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */

@Service
@RequiredArgsConstructor
@Transactional
public class FeverService implements RecordService<FeverCreateRequest, FeverGetResponse, FeverUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final FeverRepository feverRepository;
    private final FeverMapper feverMapper;

    /**
     * 새로운 발열 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 발열 기록을 생성하고,
     * 해당 정보를 `Fever` 엔터티로 저장합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 발열 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, FeverCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);

        Fever fever = Fever.builder()
                .record(record)
                .position(request.getPosition())
                .temperature(request.getTemperature())
                .build();

        feverRepository.save(fever);

        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 발열 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 발열 기록을 조회하고,
     * 변환된 {@link FeverGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 발열 기록 정보를 포함한 {@link FeverGetResponse} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public FeverGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        Fever fever = feverRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return feverMapper.mapToGetResponse(record, fever);
    }

    /**
     * 특정 발열 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 발열 기록을 업데이트합니다.
     * 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  발열 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, FeverUpdateRequest request, MultipartFile image) {
        Fever fever = feverRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        recordCommonService.updateRecord(userId, recordId, request, image);
        feverMapper.updateFeverFromUpdateRequest(request, fever);
    }

    /**
     * 특정 발열 기록을 삭제합니다.
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
        Fever fever = feverRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        feverRepository.delete(fever);
        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 발열 기록의 타입을 반환합니다.
     *
     * @return 발열 기록 타입 {@link RecordType#FEVER}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.FEVER;
    }
}
