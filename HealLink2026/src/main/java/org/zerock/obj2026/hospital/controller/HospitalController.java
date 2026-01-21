package org.zerock.obj2026.hospital.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.obj2026.doctor.service.DoctorService;
import org.zerock.obj2026.hospital.dto.HPageRequestDTO;
import org.zerock.obj2026.hospital.dto.HospitalDTO;
import org.zerock.obj2026.hospital.dto.HpageResponseDTO;
import org.zerock.obj2026.hospital.service.HospitalService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.zerock.obj2026.member.dto.UserSecurityDTO;
import org.zerock.obj2026.doctor.dto.DoctorDTO;

@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
@Log4j2
public class HospitalController {

    private final HospitalService hospitalService;
    private final DoctorService doctorService;

    @GetMapping("/list")
    public String list(HPageRequestDTO pageRequestDTO, Model model) {
        log.info("GET /hospitals/list - {}", pageRequestDTO);
        HpageResponseDTO<HospitalDTO> responseDTO = hospitalService.list(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("hPageRequestDTO", pageRequestDTO);
        return "hospital/hospitallist";
    }

    @GetMapping("/{hospitalId}")
    public String detail(@PathVariable("hospitalId") String hospitalId, 
                         Model model,
                         @AuthenticationPrincipal UserSecurityDTO userSecurityDTO) {
        log.info("GET /hospitals/{}", hospitalId);
        HospitalDTO hospitalDTO = hospitalService.getHospitalById(hospitalId);
        if (hospitalDTO == null) {
            return "redirect:/hospitals/list";
        }
        
        model.addAttribute("hospital", hospitalDTO);
        model.addAttribute("doctors", doctorService.getDoctorsByHospital(hospitalId));

        // 권한 체크: 로그인한 사용자가 이 병원의 의사인지 확인
        boolean isOwner = false;
        if (userSecurityDTO != null) {
            try {
                DoctorDTO myDoctorInfo = doctorService.getDoctorDTO(userSecurityDTO.getUser().getUserId());
                if (myDoctorInfo != null && hospitalId.equals(myDoctorInfo.getHospitalId())) {
                     isOwner = true;
                }
            } catch (Exception e) {
                log.debug("Not a doctor or mismatch: {}", e.getMessage());
            }
        }
        model.addAttribute("isOwner", isOwner);
        
        return "hospital/detail";
    }

    // 이미지 업로드 API
    @ResponseBody
    @PostMapping("/{hospitalId}/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @PathVariable String hospitalId,
            @RequestParam("file") MultipartFile file) {
            
        try {
            hospitalService.updateHospitalImage(hospitalId, file);
            return ResponseEntity.ok(Map.of("result", "success"));
        } catch (Exception e) {
            log.error("Upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("result", "fail", "message", e.getMessage()));
        }
    }

    @GetMapping
    public String redirectToHospitalList() {
        return "redirect:/hospitals/list";
    }
}