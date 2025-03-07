package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.response.MedicationGetResponse;
import com.agarang.domain.record.entity.Record;
import com.agarang.domain.record.entity.type.Antipyretic;
import com.agarang.domain.record.entity.type.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : MedicationMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-26<br>
 * description    : 복약 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-26          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MedicationMapper {
    @Mapping(source = "record.recordId", target = "recordId")
    MedicationGetResponse mapToGetResponse(Record record, Medication medication);

    @Mapping(source = "record.recordId", target = "recordId")
    MedicationGetResponse mapToGetResponse(Record record, Medication medication, Antipyretic antipyretic);
}
