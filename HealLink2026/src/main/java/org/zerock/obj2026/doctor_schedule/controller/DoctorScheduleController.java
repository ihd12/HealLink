package org.zerock.obj2026.doctor_schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.doctor_schedule.dto.DoctorScheduleDTO;
import org.zerock.obj2026.doctor_schedule.service.DoctorScheduleService;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-schedules")
@RequiredArgsConstructor
@Log4j2
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    // 스케줄 등록
    @PostMapping
    public ResponseEntity<DoctorScheduleDTO> register(@RequestBody DoctorScheduleDTO doctorScheduleDTO) {
        log.info("Registering DoctorSchedule: {}", doctorScheduleDTO);
        DoctorScheduleDTO registeredDTO = doctorScheduleService.register(doctorScheduleDTO);
        return new ResponseEntity<>(registeredDTO, HttpStatus.CREATED);
    }

    // 의사별 스케줄 조회
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleDTO>> getScheduleByDoctor(@PathVariable("doctorId") Long doctorId) {
        log.info("Getting schedules for doctorId: {}", doctorId);
        List<DoctorScheduleDTO> scheduleList = doctorScheduleService.getScheduleByDoctor(doctorId);
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    // 스케줄 상세 정보
    @GetMapping("/{scheduleId}")
    public ResponseEntity<DoctorScheduleDTO> getSchedule(@PathVariable("scheduleId") Long scheduleId) {
        log.info("Getting schedule for scheduleId: {}", scheduleId);
        DoctorScheduleDTO doctorScheduleDTO = doctorScheduleService.getSchedule(scheduleId);
        if (doctorScheduleDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctorScheduleDTO, HttpStatus.OK);
    }

    // 스케줄 수정
    @PutMapping("/{scheduleId}")
    public ResponseEntity<DoctorScheduleDTO> modify(@PathVariable("scheduleId") Long scheduleId, @RequestBody DoctorScheduleDTO doctorScheduleDTO) {
        log.info("Modifying DoctorSchedule: {} with id: {}", doctorScheduleDTO, scheduleId);
        DoctorScheduleDTO modifiedDTO = doctorScheduleService.modify(scheduleId, doctorScheduleDTO);
        if (modifiedDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modifiedDTO, HttpStatus.OK);
    }

    // 스케줄 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> remove(@PathVariable("scheduleId") Long scheduleId) {
        log.info("Removing DoctorSchedule with id: {}", scheduleId);
        doctorScheduleService.remove(scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
