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

@Controller
@RequestMapping("/hospitals")
@RequiredArgsConstructor
@Log4j2
public class HospitalController {

    private final HospitalService hospitalService;
    private final DoctorService doctorService;

    @org.springframework.beans.factory.annotation.Value("${kakao.js.key}")
    private String kakaoJsKey;

    @GetMapping("/list")
    public String list(HPageRequestDTO pageRequestDTO, Model model) {
        log.info("GET /hospitals/list - {}", pageRequestDTO);
        HpageResponseDTO<HospitalDTO> responseDTO = hospitalService.list(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("hPageRequestDTO", pageRequestDTO);
        return "hospital/hospitallist";
    }

    @GetMapping("/{hospitalId}")
    public String detail(@PathVariable("hospitalId") String hospitalId, Model model) {
        log.info("GET /hospitals/{}", hospitalId);
        HospitalDTO hospitalDTO = hospitalService.getHospitalById(hospitalId);
        if (hospitalDTO == null) {
            return "redirect:/hospitals/list";
        }
        
        model.addAttribute("hospital", hospitalDTO);
        model.addAttribute("doctors", doctorService.getDoctorsByHospital(hospitalId));
        model.addAttribute("kakaoJsKey", kakaoJsKey); // 카카오 키 전달
        
        return "hospital/detail";
    }

    @GetMapping
    public String redirectToHospitalList() {
        return "redirect:/hospitals/list";
    }
}