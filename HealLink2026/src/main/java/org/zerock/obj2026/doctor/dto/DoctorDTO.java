package org.zerock.obj2026.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.department.dto.DepartmentDTO;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long doctorId;
    private String hospitalId; // 참조용 ID
    private String name;     // User 이름
    private String hospitalName; // Hospital 병원명

    private String licenseNumber;
    private Integer careerYears;
    private String introduction;

    // 진료과 정보 (DTO로 포함)
    private Set<DepartmentDTO> departments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}