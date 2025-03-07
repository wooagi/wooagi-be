package com.agarang.domain.record.service.impl;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.mapper.BathMapper;
import com.agarang.domain.record.dto.request.BathCreateRequest;
import com.agarang.domain.record.dto.request.BathUpdateRequest;
import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.dto.response.BathGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : BathService.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-26<br>
 * description    : 목욕 기록 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-26          nature1216          최초생성<br>
 * <br>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BathService implements RecordService<BathCreateRequest, BathGetResponse, BathUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final BathMapper bathMapper;

    /**
     * 새로운 목욕 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 목욕 기록을 생성하고,
     * 생성된 기록의 ID를 반환합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 목욕 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, BathCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);

        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 목욕 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 목욕 기록을 조회하고,
     * 변환된 {@link BathGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 기록 정보를 포함한 {@link BathGetResponse} 객체
     */
    @Override
    public BathGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        return bathMapper.mapToBathGetResponse(record);
    }

    /**
     * 특정 목욕 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 목욕 기록을 업데이트합니다.
     * 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  목욕 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, BathUpdateRequest request, MultipartFile image) {
        recordCommonService.updateRecord(userId, recordId, request, image);
    }

    /**
     * 특정 목욕 기록을 삭제합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 기록을 삭제합니다.
     * </p>
     *
     * @param userId   기록을 삭제하는 사용자 ID
     * @param recordId 삭제할 기록의 ID
     */
    @Override
    public void deleteRecord(Integer userId, Integer recordId) {
        recordCommonService.deleteRecord(userId, recordId);
    }

    /**
     * 목욕 기록의 타입을 반환합니다.
     *
     * @return 목욕 기록 타입 {@link RecordType#BATH}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.BATH;
    }
}
