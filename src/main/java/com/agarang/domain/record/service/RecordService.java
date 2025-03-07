package com.agarang.domain.record.service;

import com.agarang.domain.baby.entity.Baby;
import com.agarang.domain.record.dto.request.BaseRecordUpdateRequest;
import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.dto.response.BaseRecordGetResponse;
import com.agarang.domain.record.dto.response.RecordCreateResponse;
import com.agarang.domain.record.entity.enumeration.RecordType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


/**
 * packageName    : com.agarang.domain.record.service<br>
 * fileName       : RecordService.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-22<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-22          nature1216          최초생성<br>
 * <br>
 */
public interface RecordService<C extends BaseRecordCreateRequest, R extends BaseRecordGetResponse, Q extends BaseRecordUpdateRequest> {
    RecordCreateResponse createRecord(Integer userId, Integer babyId, C request);
    R getRecord(Integer userId, Integer recordId);
    void updateRecord(Integer userId, Integer recordId, Q request, MultipartFile image);
    void deleteRecord(Integer userId, Integer recordId);
    RecordType getRecordType();
}
