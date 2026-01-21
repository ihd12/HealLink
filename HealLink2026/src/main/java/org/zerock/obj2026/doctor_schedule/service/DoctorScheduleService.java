package org.zerock.obj2026.doctor_schedule.service;

import org.zerock.obj2026.doctor_schedule.dto.DoctorScheduleDTO;

import java.util.List;

public interface DoctorScheduleService {
    DoctorScheduleDTO register(DoctorScheduleDTO doctorScheduleDTO);
//      특정 의사의 모든 근무 일정 조회
    List<DoctorScheduleDTO> getScheduleByDoctor(Long doctorId);
//      하나의 일정 상세 조회 일정의 고유 ID 로 조회
    DoctorScheduleDTO getSchedule(Long scheduleId);
//     일정 수정 어떤 일정을 수정할지(scheduleId)수정할 내용(doctorScheduleDTO) 수정 후 결과를 반환
    DoctorScheduleDTO modify(Long scheduleId, DoctorScheduleDTO doctorScheduleDTO);
//    일정삭제
    void remove(Long scheduleId);
}
