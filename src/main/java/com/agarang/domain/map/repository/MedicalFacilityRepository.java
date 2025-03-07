package com.agarang.domain.map.repository;

import com.agarang.domain.map.entity.MedicalFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.agarang.domain.map.repository<br>
 * fileName       : MedicalFacilityRepository.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : MedicalFacility 엔티티의 repository 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03          okeio           최초생성<br>
 * 25.02.06          okeio           조회 메서드 분류<br>
 * <br>
 */
@Repository
public interface MedicalFacilityRepository extends JpaRepository<MedicalFacility, Long> {
    @Query(value = """
        SELECT *, 
               ST_Distance_Sphere(
                   POINT(mf.longitude, mf.latitude), 
                   POINT(:lng, :lat)
               ) AS distance
        FROM medical_facility mf
        WHERE ST_Distance_Sphere(
                  POINT(mf.longitude, mf.latitude), 
                  POINT(:lng, :lat)
              ) <= :radius
                AND mf.type_code = :typeCode
        ORDER BY distance
        """, nativeQuery = true)
    List<MedicalFacility> findHospitalsWithinRadiusByType(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("radius") Double radius,
            @Param("typeCode") Integer typeCode
    );

    @Query(value = """
        SELECT *, 
               ST_Distance_Sphere(
                   POINT(mf.longitude, mf.latitude), 
                   POINT(:lng, :lat)
               ) AS distance
        FROM medical_facility mf
        WHERE ST_Distance_Sphere(
                  POINT(mf.longitude, mf.latitude), 
                  POINT(:lng, :lat)
              ) <= :radius
                AND mf.medical_dept_code = :medicalDeptCode
        ORDER BY distance
        """, nativeQuery = true)
    List<MedicalFacility> findHospitalsWithinRadiusByMedicalDeptCode(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("radius") Double radius,
            @Param("medicalDeptCode") Integer medicalDeptCode
    );
}
