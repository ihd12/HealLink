package org.zerock.obj2026.patient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.obj2026.member.dto.UserSecurityDTO;
import org.zerock.obj2026.patient.dto.PatientDTO;
import org.zerock.obj2026.patient.service.PatientService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class PatientController {
    private final PatientService patientService;

    // 환자 정보 조회
    @GetMapping("/patient")
    public String getPatient(@AuthenticationPrincipal UserSecurityDTO user, Model model){
        model.addAttribute("patient", patientService.getPatient(user.getUser().getUserId()));
        return "mypage/patient";
    }
    // 환자 정보 DB 입력
    @PostMapping("/patient")
    public String postAddPatient(PatientDTO dto, @AuthenticationPrincipal UserSecurityDTO userSecurityDTO){
        PatientDTO result = patientService.addPatient(dto, userSecurityDTO.getUser().getEmail());
        return "redirect:/mypage/patient";
    }
    // 환자 정보 수정 창 (사용안함)
    @GetMapping("/edit/{id}")
    public String getEditPatient(@PathVariable Long patientId, Model model){
        model.addAttribute("patient", patientService.getPatient(patientId));
        return "mypage/patient";
    }
    // 환자 정보 DB수정 (사용안함)
    @PostMapping("/edit/{id}")
    public String postEditPatient(@PathVariable Long patientId, PatientDTO dto
            , @AuthenticationPrincipal UserSecurityDTO userSecurityDTO){
        patientService.editPatient(dto, userSecurityDTO.getUser());
        return "redirect:/patient/"+dto.getPatientId();
    }
}
