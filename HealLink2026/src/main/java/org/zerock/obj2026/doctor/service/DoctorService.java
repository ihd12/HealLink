package org.zerock.obj2026.doctor.service;

import org.zerock.obj2026.doctor.dto.DoctorDTO;
import java.util.List;

public interface DoctorService {
    // 병원 ID로 소속 의사 목록 조회
    List<DoctorDTO> getDoctorsByHospital(String hpid);

    // 의사 ID로 상세 정보 조회 후 DTO 반환
    DoctorDTO getDoctorDTO(Long doctorId);
}
