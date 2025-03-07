package com.agarang.domain.map.service;

import com.agarang.domain.map.dto.mapper.MapMapper;
import com.agarang.domain.map.dto.request.LocationRequest;
import com.agarang.domain.map.dto.response.MedicalFacilityResponse;
import com.agarang.domain.map.dto.response.NursingRoomResponse;
import com.agarang.domain.map.entity.MedicalFacility;
import com.agarang.domain.map.entity.NursingRoom;
import com.agarang.domain.map.repository.MedicalFacilityRepository;
import com.agarang.domain.map.repository.NursingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.agarang.domain.map.service<br>
 * fileName       : MapService.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 4.<br>
 * description    : 위치 기반 정보를 제공하는, MedicalFacility, NursingRoom 엔티티의 서비스 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.04          okeio           최초생성<br>
 * 25.02.07          okeio           산부인과 데이터 조회 추가<br>
 * <br>
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MapService {
    private final MedicalFacilityRepository medicalFacilityRepository;
    private final NursingRoomRepository nursingRoomRepository;
    private final MapMapper mapMapper;

    private static final int OBSTETRICS_MEDICAL_DEPT_CODE = 10;
    private static final int PEDIATRICS_MEDICAL_DEPT_CODE = 11;
    private static final int PHARMACY_TYPE_CODE = 81;

    /**
     * 반경 몇 m 이내의 소아청소년과 진료 병원을 조회합니다.
     *
     * @param locationRequest 사용자의 위치 및 반경
     * @return 의료시설(소아청소년과) 정보 {@link MedicalFacilityResponse}
     */
    public List<MedicalFacilityResponse> getPediatricsData(LocationRequest locationRequest) {
        List<MedicalFacility> list = medicalFacilityRepository.findHospitalsWithinRadiusByMedicalDeptCode(
                locationRequest.getLatitude(),
                locationRequest.getLongitude(),
                locationRequest.getRadius().doubleValue(),
                PEDIATRICS_MEDICAL_DEPT_CODE
        );

        return mapMapper.mapToMedicalFacilitiyResponseList(list);
    }

    /**
     * 반경 몇 m 이내의 산부인과 진료 병원를 조회합니다.
     *
     * @param locationRequest 사용자의 위치 및 반경
     * @return 의료시설(산부인과) 정보 {@link MedicalFacilityResponse}
     */
    public List<MedicalFacilityResponse> getObstetricsData(LocationRequest locationRequest) {
        List<MedicalFacility> list = medicalFacilityRepository.findHospitalsWithinRadiusByMedicalDeptCode(
                locationRequest.getLatitude(),
                locationRequest.getLongitude(),
                locationRequest.getRadius().doubleValue(),
                OBSTETRICS_MEDICAL_DEPT_CODE
        );

        return mapMapper.mapToMedicalFacilitiyResponseList(list);
    }

    /**
     * 반경 몇 m 이내의 약국을 조회합니다.
     *
     * @param locationRequest 사용자의 위치 및 반경
     * @return 의료시설(약국) 정보 {@link MedicalFacilityResponse}
     */
    public List<MedicalFacilityResponse> getPharmaciesData(LocationRequest locationRequest) {
        List<MedicalFacility> list = medicalFacilityRepository.findHospitalsWithinRadiusByType(
                locationRequest.getLatitude(),
                locationRequest.getLongitude(),
                locationRequest.getRadius().doubleValue(),
                PHARMACY_TYPE_CODE
        );

        return mapMapper.mapToMedicalFacilitiyResponseList(list);
    }

    /**
     * 반경 몇 m 이내의 수유실을 조회합니다.
     *
     * @param locationRequest 사용자의 위치 및 반경
     * @return 수유실 정보 {@link NursingRoomResponse}
     */
    public List<NursingRoomResponse> getNursingRoomData(LocationRequest locationRequest) {
        List<NursingRoom> list = nursingRoomRepository.findNursingRoomsWithinRadius(
                locationRequest.getLatitude(),
                locationRequest.getLongitude(),
                locationRequest.getRadius().doubleValue()
        );

        return list.stream()
                .map(mapMapper::mapToNursingRoomResponse)
                .toList();
    }
}
