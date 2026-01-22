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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ModelMapper modelMapper;

    @Value("${org.zerock.upload.path}")
    private String uploadPath;
    
    private static final List<String> TAG_POOL = Arrays.asList(
        "#친절해요", "#주차가능", "#전문의상주", "#최신시설", "#야간진료", 
        "#깨끗해요", "#대기짧음", "#꼼꼼해요", "#과잉진료X", "#설명잘함"
    );

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

        // 시간 포맷팅
        dto.setMonTime(getOperationTime(hospital.getDutyTime1s(), hospital.getDutyTime1c()));
        dto.setTueTime(getOperationTime(hospital.getDutyTime2s(), hospital.getDutyTime2c()));
        dto.setWedTime(getOperationTime(hospital.getDutyTime3s(), hospital.getDutyTime3c()));
        dto.setThuTime(getOperationTime(hospital.getDutyTime4s(), hospital.getDutyTime4c()));
        dto.setFriTime(getOperationTime(hospital.getDutyTime5s(), hospital.getDutyTime5c()));
        dto.setSatTime(getOperationTime(hospital.getDutyTime6s(), hospital.getDutyTime6c()));
        dto.setSunTime(getOperationTime(hospital.getDutyTime7s(), hospital.getDutyTime7c()));
        dto.setHolidayTime(getOperationTime(hospital.getDutyTime8s(), hospital.getDutyTime8c()));
        
        // 별점
        double rating = 3.5 + (Math.random() * 1.5);
        dto.setRating(Math.round(rating * 10) / 10.0);

        //  리뷰 수
        dto.setReviewCount((int) (Math.random() * 490) + 10);

        //  태그
        List<String> shuffledTags = new ArrayList<>(TAG_POOL);
        Collections.shuffle(shuffledTags);
        dto.setTags(shuffledTags.subList(0, 3));
        
        // 병원 이미지
        int randomImgNum = (int) (Math.random() * 10) + 1;
        dto.setImg("Dororo" + randomImgNum + ".jpg");
        
        // 현재 진료 상태 계산
        dto.setStatus(calculateCurrentStatus(hospital));

        return dto;
    }
    
    private String calculateCurrentStatus(Hospital hospital) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        int dayOfWeek = now.getDayOfWeek().getValue(); // 1(월) ~ 7(일)
        int currentTime = now.getHour() * 100 + now.getMinute(); // 14:30 -> 1430
        
        Integer start = null;
        Integer end = null;
        
        // 오늘 요일에 맞는 시간 가져오기
        switch (dayOfWeek) {
            case 1: start = hospital.getDutyTime1s(); end = hospital.getDutyTime1c(); break;
            case 2: start = hospital.getDutyTime2s(); end = hospital.getDutyTime2c(); break;
            case 3: start = hospital.getDutyTime3s(); end = hospital.getDutyTime3c(); break;
            case 4: start = hospital.getDutyTime4s(); end = hospital.getDutyTime4c(); break;
            case 5: start = hospital.getDutyTime5s(); end = hospital.getDutyTime5c(); break;
            case 6: start = hospital.getDutyTime6s(); end = hospital.getDutyTime6c(); break;
            case 7: start = hospital.getDutyTime7s(); end = hospital.getDutyTime7c(); break;
        }
        
        // TODO: 공휴일 체크 로직은 별도 구현 필요 (일단 일요일과 구분 없이 처리)
        
        if (start == null || end == null) {
            return "휴진";
        }
        
        if (currentTime >= start && currentTime < end) {
            return "진료중";
        } else {
            return "진료마감";
        }
    }

    private String getOperationTime(Integer start, Integer end) {
        if (start == null || end == null) return "휴진";
        return formatTime(start) + " ~ " + formatTime(end);
    }

    private String formatTime(Integer time) {
        if (time == null) return null;
        String str = String.format("%04d", time); // 900 -> "0900"
        return str.substring(0, 2) + ":" + str.substring(2); // "09:00"
    }
}
