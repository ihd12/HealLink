package org.zerock.obj2026.appointment.service;

public interface AppointmentService {
    /* 예약 생성.
      @param appointmentDTO 예약 정보 DTO (workDate, startTime, doctorId 포함)
      @return 생성된 예약의 ID */
    Long createAppointment(org.zerock.obj2026.appointment.dto.AppointmentDTO appointmentDTO);
}
