package com.agarang.domain.map.repository;

import com.agarang.domain.map.entity.NursingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.agarang.domain.map.repository<br>
 * fileName       : NursingRoomRepository.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : NursingRoomData 엔티티의 repository 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03         okeio           최초생성<br>
 * <br>
 */
@Repository
public interface NursingRoomRepository extends JpaRepository<NursingRoom, Long> {
    @Query(value = """
        SELECT *,
               ST_Distance_Sphere(
                   POINT(nr.longitude, nr.latitude),
                   POINT(:lng, :lat)
               ) AS distance
        FROM nursing_room nr 
        WHERE ST_Distance_Sphere(
                  POINT(nr.longitude, nr.latitude),
                  POINT(:lng, :lat)
              ) <= :radius
        ORDER BY distance
        """, nativeQuery = true)
    List<NursingRoom> findNursingRoomsWithinRadius(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("radius") Double radius
    );
}
