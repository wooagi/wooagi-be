package com.agarang.domain.record.service;

import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.dto.request.BaseRecordUpdateRequest;
import com.agarang.domain.record.dto.response.BaseRecordGetResponse;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.repository.RecordRepository;
import com.agarang.domain.record.service.factory.RecordServiceFactory;
import com.agarang.global.exception.BusinessException;
import com.agarang.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * packageName    : com.agarang.domain.record.service<br>
 * fileName       : RecordLookupService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 카테고리에 해당하는 service 객체를 반환하는 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */
@Service
@RequiredArgsConstructor
public class RecordLookupService {
    private final RecordRepository recordRepository;
    private final RecordServiceFactory recordServiceFactory;

    /**
     * 주어진 기록 타입에 해당하는 기록 서비스 객체를 반환합니다.
     *
     * <p>
     * 주어진 `RecordType`을 기반으로 적절한 기록 서비스 객체를 가져옵니다.
     * </p>
     *
     * @param recordType 조회할 기록의 타입
     * @return 해당 기록 타입에 맞는 {@link RecordService} 객체
     */
    public RecordService<BaseRecordCreateRequest, BaseRecordGetResponse, BaseRecordUpdateRequest> getRecordService(RecordType recordType) {
        return recordServiceFactory.getService(recordType);
    }

    /**
     * 특정 기록 ID에 해당하는 기록 서비스 객체를 반환합니다.
     *
     * <p>
     * 기록 ID를 기반으로 기록 타입을 조회한 후, 적절한 기록 서비스 객체를 가져옵니다.
     * </p>
     *
     * @param recordId 조회할 기록의 ID
     * @return 해당 기록 ID에 맞는 {@link RecordService} 객체
     * @throws BusinessException 기록을 찾을 수 없는 경우 발생
     */
    public RecordService<BaseRecordCreateRequest, BaseRecordGetResponse, BaseRecordUpdateRequest> getRecordService(Integer recordId) {
        RecordType recordType = recordRepository.findRecordTypeById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        return recordServiceFactory.getService(recordType);
    }
}
