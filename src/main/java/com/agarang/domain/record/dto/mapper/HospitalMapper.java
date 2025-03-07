package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.HospitalUpdateRequest;
import com.agarang.domain.record.dto.response.HospitalGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Hospital;
import org.mapstruct.*;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : HospitalMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-27<br>
 * description    : 병원 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-27          nature1216          최초생성<br>
 * <br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface HospitalMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHospitalFromUpdateRequest(HospitalUpdateRequest request, @MappingTarget Hospital hospital);

    @Mapping(source = "record.recordId", target = "recordId")
    HospitalGetResponse mapToGetResponse(Record record, Hospital hospital);
}
