package com.agarang.domain.map.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.agarang.domain.map.entity<br>
 * fileName       : NursingRoomData.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : NursingRoomData Entity 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03          okeio           최초생성<br>
 * 25.02.06          okeio           일부 필드 추가 및 순서 변경<br>
 * <br>
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "nursing_room")
public class NursingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_no", nullable = false, unique = true)
    private String roomNo;

    private String name;

    private String cityName;

    private String zoneName;

    private String townName;

    @Column(name = "room_type_code")
    private Integer roomTypeCode; // 수유실 종류 코드

    @Column(name = "room_type_name")
    private String roomTypeName;

    private String phone;

    private String address; // 주소

    private String location; // 상세 위치

    @Column(name = "father_use_code")
    private Integer fatherUseCode; // 아빠 이용 코드

    @Column(name = "father_use_name")
    private String fatherUseName;

    private Double latitude;

    private Double longitude;

}
