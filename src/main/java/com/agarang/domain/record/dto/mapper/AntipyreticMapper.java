package com.agarang.domain.record.dto.mapper;

import com.agarang.domain.record.dto.request.MedicationUpdateRequest;
import com.agarang.domain.record.entity.type.Antipyretic;
import org.mapstruct.*;
/**
 * packageName    : com.agarang.domain.record.dto.mapper<br>
 * fileName       : AntipyreticMapper.java<br>
 * author         : nature1216 <br>
 * date           : 2025-01-24<br>
 * description    : 해열제 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2025-01-24          nature1216          최초생성<br>
 * <br>
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AntipyreticMapper {
    /**
     * 해열제(`Antipyretic`) 데이터를 업데이트합니다.
     *
     * <p>
     * 이 메서드는 `MedicationUpdateRequest` 객체의 값을 사용하여 `Antipyretic` 엔터티를 업데이트합니다.
     * `null` 값이 포함된 속성은 무시되며, 기존 값을 유지합니다.
     * </p>
     *
     * @param request      업데이트할 데이터를 포함하는 요청 객체
     * @param antipyretic  업데이트할 대상 `Antipyretic` 엔터티
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAntipyreticFromUpdateRequest(MedicationUpdateRequest request, @MappingTarget Antipyretic antipyretic);

}
