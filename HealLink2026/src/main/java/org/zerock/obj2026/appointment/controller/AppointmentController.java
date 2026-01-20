package org.zerock.obj2026.appointment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.appointment.dto.AppointmentDTO;
import org.zerock.obj2026.appointment.service.AppointmentService;
import org.zerock.obj2026.doctor.dto.DoctorDTO;
import org.zerock.obj2026.doctor.service.DoctorService;
import org.zerock.obj2026.member.dto.UserSecurityDTO;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    // 예약 화면 띄우기
    @GetMapping("/appointments/form/{doctorId}")
    public String appointmentForm(@PathVariable Long doctorId, Model model) {
        DoctorDTO doctorDTO = doctorService.getDoctorDTO(doctorId);
        
        model.addAttribute("doctor", doctorDTO);
        return "son/ucalendar";
    }

    // 예약 생성 API
    @ResponseBody
    @PostMapping("/api/appointments")
    public ResponseEntity<Long> createAppointment(@RequestBody AppointmentDTO appointmentDTO, 
                                                  @AuthenticationPrincipal UserSecurityDTO userSecurityDTO) {
        log.info("Received appointment request: {}", appointmentDTO);

        if (userSecurityDTO == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Long patientId = userSecurityDTO.getUser().getUserId();
            
            Long appointmentId = appointmentService.createAppointment(
                    appointmentDTO.getScheduleId(),
                    patientId,
                    appointmentDTO.getDepartmentId(),
                    appointmentDTO.getSymptom(),
                    appointmentDTO.getNote()
            );

            return new ResponseEntity<>(appointmentId, HttpStatus.CREATED);

        } catch (IllegalStateException e) {
            log.warn("Appointment failed: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Error creating appointment", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
