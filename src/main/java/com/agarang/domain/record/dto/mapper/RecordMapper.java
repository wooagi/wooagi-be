package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.BaseRecordUpdateRequest;
import com.agarang.domain.record.dto.response.BaseRecordGetResponse;
import com.agarang.domain.record.entity.Record;
import org.mapstruct.*;

import java.util.List;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : RecordMapper.java<br>
 * author         : nature1216 <br>
 * date           : 1/24/25<br>
 * description    : 기록 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 1/24/25          nature1216          최초생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RecordMapper {

    /**
     * 기록 데이터를 업데이트합니다.
     *
     * <p>
     * `BaseRecordUpdateRequest` 객체의 값을 사용하여 `Record` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request 업데이트할 데이터를 포함하는 요청 객체
     * @param record  업데이트할 대상 `Record` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecordFromBaseUpdateRequest(BaseRecordUpdateRequest request, @MappingTarget Record record);
}
