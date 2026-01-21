package org.zerock.obj2026.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.appointment.domain.AppointmentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AppointmentDTO {
    private Long appointmentId;
    private Long patientId; // PatientDTO에서 patientId를 참조함
    private Long scheduleId; // DoctorScheduleDTO에서 scheduleId를 참조함
    private Long departmentId;
    private LocalDateTime appointmentDatetime;
    private String symptom;
    private String note;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
