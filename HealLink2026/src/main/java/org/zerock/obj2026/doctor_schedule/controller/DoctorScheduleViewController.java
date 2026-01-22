package org.zerock.obj2026.doctor_schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
@Log4j2
public class DoctorScheduleViewController {

    @GetMapping("/schedule")
    public String scheduleManagement() {
        log.info("Doctor Schedule Management Page (dcalendar)");
        return "son/dcalendar";
    }
}
