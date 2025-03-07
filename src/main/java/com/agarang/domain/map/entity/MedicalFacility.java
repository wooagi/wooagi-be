package com.agarang.domain.map.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.agarang.domain.map.entity<br>
 * fileName       : MedicalFacility.java<br>
 * author         : okeio<br>
 * date           : 25. 2. 3.<br>
 * description    : MedicalFacility 엔티티 클래스입니다. <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 25.02.03          okeio           최초생성<br>
 * 25.02.07          okeio           복합 unique 키 및 칼럼 추가<br>
 * <br>
 */
@Entity
@Table(name = "medical_facility",
        uniqueConstraints = { @UniqueConstraint(name = "UniqueDeptCodeAndId", columnNames = { "dept_code_normalized", "encrypted_id" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encrypted_id", nullable = false)
    private String encryptedId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type_code")
    private Integer typeCode;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "postal_code")
    private String postalCode;

    private String address;

    private String phone;

    private Double latitude;

    private Double longitude;

    @Column(name = "medical_dept_code")
    private Integer medicalDeptCode;

    @Column(name = "medical_dept_name")
    private String medicalDeptName;

    @Column(name = "dept_code_normalized", insertable = false, updatable = false)
    private Integer deptCodeNormalized;

}


