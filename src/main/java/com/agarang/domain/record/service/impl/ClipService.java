package com.agarang.domain.record.service.impl;

import com.agarang.domain.alarm.event.FcmTokenSendMessageEvent;
import com.agarang.domain.record.dto.mapper.ClipMapper;
import com.agarang.domain.record.dto.request.ClipCreateRequest;
import com.agarang.domain.record.dto.request.ClipUpdateRequest;
import com.agarang.domain.record.dto.response.ClipGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.service.RecordCommonService;
import com.agarang.domain.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * packageName    : com.agarang.domain.record.service.impl<br>
 * fileName       : ClipService.java<br>
 * author         : nature1216 <br>
 * date           : 1/26/25<br>
 * description    : 클립 service 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/26/25          nature1216          최초생성<br>
 */

@Service
@RequiredArgsConstructor
@Transactional
public class ClipService implements RecordService<ClipCreateRequest, ClipGetResponse, ClipUpdateRequest> {
    private final RecordCommonService recordCommonService;
    private final ClipMapper clipMapper;
    private final ApplicationEventPublisher eventPublisher;

    public static final String NOTIFICATION_MESSAGE = " 클립이 도착했어요!";

    /**
     * 새로운 클립 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 클립 기록을 생성하고,
     * FCM 푸시 알림 이벤트를 발행한 후, 생성된 기록의 ID를 반환합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 클립 기록 생성 요청 객체
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    @Override
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, ClipCreateRequest request) {
        Record record = recordCommonService.createRecord(userId, babyId, request);
        eventPublisher.publishEvent(new FcmTokenSendMessageEvent(this, babyId, record.getBaby().getName() + NOTIFICATION_MESSAGE, record.getContent()));
        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 이미지와 함께 새로운 클립 기록을 생성합니다.
     *
     * <p>
     * 주어진 사용자 ID와 아기 ID를 기반으로 클립 기록을 생성하고,
     * 이미지를 저장한 후, FCM 푸시 알림 이벤트를 발행합니다.
     * 생성된 기록의 ID를 반환합니다.
     * </p>
     *
     * @param userId  기록을 생성하는 사용자 ID
     * @param babyId  기록을 생성할 아기의 ID
     * @param request 클립 기록 생성 요청 객체
     * @param image   기록과 함께 저장할 이미지 파일 (선택 사항)
     * @return 생성된 기록의 정보를 포함한 {@link RecordCreateResponse} 객체
     */
    public RecordCreateResponse createRecord(Integer userId, Integer babyId, ClipCreateRequest request, MultipartFile image) {
        Record record = recordCommonService.createRecord(userId, babyId, request, image);
        eventPublisher.publishEvent(new FcmTokenSendMessageEvent(this, babyId, record.getBaby().getName() + NOTIFICATION_MESSAGE, record.getContent()));
        return RecordCreateResponse.builder()
                .recordId(record.getRecordId())
                .build();
    }

    /**
     * 특정 클립 기록을 조회합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 해당 클립 기록을 조회하고,
     * 변환된 {@link ClipGetResponse} 객체를 반환합니다.
     * </p>
     *
     * @param userid   기록을 조회하는 사용자 ID
     * @param recordId 조회할 기록의 ID
     * @return 조회된 클립 기록 정보를 포함한 {@link ClipGetResponse} 객체
     */
    @Override
    public ClipGetResponse getRecord(Integer userid, Integer recordId) {
        Record record = recordCommonService.getRecord(userid, recordId);

        return clipMapper.mapToGetResponse(record);
    }

    /**
     * 특정 클립 기록을 업데이트합니다.
     *
     * <p>
     * 주어진 사용자 ID와 기록 ID를 기반으로 클립 기록을 업데이트합니다.
     * 필요한 경우 이미지를 함께 수정할 수 있습니다.
     * </p>
     *
     * @param userId   기록을 업데이트하는 사용자 ID
     * @param recordId 수정할 기록의 ID
     * @param request  클립 기록 수정 요청 객체
     * @param image    업데이트할 이미지 파일 (선택 사항)
     */
    @Override
    public void updateRecord(Integer userId, Integer recordId, ClipUpdateRequest request, MultipartFile image) {
        recordCommonService.updateRecord(userId, recordId, request, image);
    }

    /**
     * 특정 클립 기록을 삭제합니다.
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
     * 클립 기록의 타입을 반환합니다.
     *
     * @return 클립 기록 타입 {@link RecordType#CLIP}
     */
    @Override
    public RecordType getRecordType() {
        return RecordType.CLIP;
    }
}
