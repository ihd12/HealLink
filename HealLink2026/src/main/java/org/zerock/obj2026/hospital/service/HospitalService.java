package org.zerock.obj2026.hospital.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.obj2026.hospital.dto.HPageRequestDTO;
import org.zerock.obj2026.hospital.dto.HpageResponseDTO;
import org.zerock.obj2026.hospital.dto.HospitalDTO;

public interface HospitalService {

    HpageResponseDTO<HospitalDTO> list(HPageRequestDTO requestDTO);

    HpageResponseDTO<HospitalDTO> getHospitalsByRegion(
            String sido,
            String gu,
            String dong,
            Pageable pageable
    );

    HospitalDTO getHospitalById(String hospitalId);
    
    void updateHospitalImage(String hpid, MultipartFile file);
}
