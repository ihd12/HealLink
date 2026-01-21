package org.zerock.obj2026.hospital.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.hospital.domain.Hospital;
import org.zerock.obj2026.hospital.dto.HPageRequestDTO;
import org.zerock.obj2026.hospital.dto.HospitalDTO;
import org.zerock.obj2026.hospital.dto.HpageResponseDTO;
import org.zerock.obj2026.hospital.repository.HospitalRepository;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ModelMapper modelMapper;

    //  키워드 기반 검색
    @Override
    public HpageResponseDTO<HospitalDTO> list(HPageRequestDTO requestDTO) {

        Page<Hospital> result =
                hospitalRepository.search(
                        requestDTO.splitTypes(),
                        requestDTO.getKeyword(),
                        requestDTO.getPageable()
                );

        Page<HospitalDTO> dtoPage =
                result.map(hospital ->
                        modelMapper.map(hospital, HospitalDTO.class)
                );

        return HpageResponseDTO.of(dtoPage);

    }

    // 지역 기반 병원 조회
    @Override
    public HpageResponseDTO<HospitalDTO> getHospitalsByRegion(
            String sido,
            String gu,
            String dong,
            Pageable pageable) {

        Page<Hospital> result =
                hospitalRepository.findHospitalsByRegion(
                        sido, gu, dong, pageable
                );

        Page<HospitalDTO> dtoPage =
                result.map(hospital ->
                        modelMapper.map(hospital, HospitalDTO.class)
                );

        return HpageResponseDTO.of(dtoPage);
    }

    // 병원 상세
    @Override
    public HospitalDTO getHospitalById(String hospitalId) {

        return hospitalRepository.findById(hospitalId)
                .map(hospital ->
                        modelMapper.map(hospital, HospitalDTO.class)
                )
                .orElse(null);
    }
}
