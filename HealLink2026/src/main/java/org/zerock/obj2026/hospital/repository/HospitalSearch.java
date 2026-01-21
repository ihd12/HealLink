package org.zerock.obj2026.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.obj2026.hospital.domain.Hospital;
import org.zerock.obj2026.hospital.dto.HPageRequestDTO;

public interface HospitalSearch {
    Page<Hospital> search(HPageRequestDTO requestDTO);

    Page<Hospital> findHospitalsByRegion(
            String sido,
            String gu,
            String dong,
            Pageable pageable
    );
}
