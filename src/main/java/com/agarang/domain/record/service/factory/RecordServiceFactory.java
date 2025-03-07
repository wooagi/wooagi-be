package com.agarang.domain.record.service.factory;

import com.agarang.domain.record.dto.request.BaseRecordCreateRequest;
import com.agarang.domain.record.dto.request.BaseRecordUpdateRequest;
import com.agarang.domain.record.dto.response.BaseRecordGetResponse;
import com.agarang.domain.record.entity.enumeration.RecordType;
import com.agarang.domain.record.service.RecordService;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.agarang.domain.record.service.factory<br>
 * fileName       : RecordServiceFactory.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-23<br>
 * description    : 카테고리에 따라 서비스를 매핑하는 팩토리 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-23          nature1216          최초생성<br>
 * <br>
 */
@Component
public class RecordServiceFactory {
    private final Map<RecordType, RecordService<?, ?, ?>> recordServiceMap = new EnumMap<>(RecordType.class);

    public RecordServiceFactory(List<RecordService<?, ?, ?>> recordServices) {
        for (RecordService<?, ?, ?> recordService : recordServices) {
            recordServiceMap.put(recordService.getRecordType(), recordService);
        }
    }

    public <C extends BaseRecordCreateRequest, R extends BaseRecordGetResponse, Q extends BaseRecordUpdateRequest> RecordService<C, R, Q> getService(RecordType type) {
        return (RecordService<C, R, Q>) recordServiceMap.get(type);
    }
}
