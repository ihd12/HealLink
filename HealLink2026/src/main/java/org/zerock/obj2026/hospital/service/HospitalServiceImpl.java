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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ModelMapper modelMapper;

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @Transactional
    @Override
    public void updateHospitalImage(String hpid, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + "_" + originalName;
        
        Path savePath = Paths.get(uploadPath, saveName);

        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            file.transferTo(savePath);
            
            Hospital hospital = hospitalRepository.findById(hpid)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Hospital ID: " + hpid));
            
            hospital.updateImage(saveName); 

        } catch (IOException e) {
            log.error("Image upload failed", e);
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public HpageResponseDTO<HospitalDTO> list(HPageRequestDTO requestDTO) {

        Page<Hospital> result =
                hospitalRepository.search(requestDTO);

        Page<HospitalDTO> dtoPage = result.map(this::entityToDTO);

        return HpageResponseDTO.of(dtoPage);
    }

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

        Page<HospitalDTO> dtoPage = result.map(this::entityToDTO);

        return HpageResponseDTO.of(dtoPage);
    }

    // 병원 상세
    @Override
    public HospitalDTO getHospitalById(String hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .map(this::entityToDTO)
                .orElse(null);
    }

    private HospitalDTO entityToDTO(Hospital hospital) {
        HospitalDTO dto = modelMapper.map(hospital, HospitalDTO.class);

        dto.setMonTime(getOperationTime(hospital.getDutyTime1s(), hospital.getDutyTime1c()));
        dto.setTueTime(getOperationTime(hospital.getDutyTime2s(), hospital.getDutyTime2c()));
        dto.setWedTime(getOperationTime(hospital.getDutyTime3s(), hospital.getDutyTime3c()));
        dto.setThuTime(getOperationTime(hospital.getDutyTime4s(), hospital.getDutyTime4c()));
        dto.setFriTime(getOperationTime(hospital.getDutyTime5s(), hospital.getDutyTime5c()));
        dto.setSatTime(getOperationTime(hospital.getDutyTime6s(), hospital.getDutyTime6c()));
        dto.setSunTime(getOperationTime(hospital.getDutyTime7s(), hospital.getDutyTime7c()));
        dto.setHolidayTime(getOperationTime(hospital.getDutyTime8s(), hospital.getDutyTime8c()));

        return dto;
    }

    private String getOperationTime(Integer start, Integer end) {
        if (start == null || end == null) return "휴진";
        return formatTime(start) + " ~ " + formatTime(end);
    }

    private String formatTime(Integer time) {
        if (time == null) return null;
        String str = String.format("%04d", time); // 900 -> "0900" 시간출력 위한변경
        return str.substring(0, 2) + ":" + str.substring(2); // "09:00"
    }
}
