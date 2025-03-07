package com.agarang.domain.map.dto.mapper;

import com.agarang.domain.map.dto.response.MedicalFacilityResponse;
import com.agarang.domain.map.dto.response.NursingRoomResponse;
import com.agarang.domain.map.entity.MedicalFacility;
import com.agarang.domain.map.entity.NursingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.agarang.domain.map.dto.mapper<br>
 * fileName       : MapMapper.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 4.<br>
 * description    : 위치 기반 엔티티를 DTO로 변환하는 Mapper 인터페이스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.04          okeio           최초생성<br>
 * <br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MapMapper {
    /**
     * 의료 시설 엔티티 리스트를 DTO 리스트로 변환합니다.
     *
     * <p>
     * 의료 시설 리스트를 `MedicalFacilityResponse` 리스트로 변환하여 반환합니다.
     * </p>
     *
     * @param facilities 변환할 의료 시설 엔티티 리스트
     * @return 변환된 {@link MedicalFacilityResponse} 리스트
     */
    List<MedicalFacilityResponse> mapToMedicalFacilitiyResponseList(List<MedicalFacility> facilities);

    /**
     * 수유실 엔티티를 DTO로 변환합니다.
     *
     * <p>
     * 수유실 엔티티를 `NursingRoomResponse` DTO로 변환하여 반환합니다.
     * </p>
     *
     * @param entity 변환할 수유실 엔티티
     * @return 변환된 {@link NursingRoomResponse} DTO
     */
    NursingRoomResponse mapToNursingRoomResponse(NursingRoom entity);
}
