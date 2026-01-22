package org.zerock.obj2026.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.appointment.domain.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private Long patientId; 
    private Long doctorId; // 의사 ID (동적 생성 시 필수)
    private Long scheduleId;
    
    // 동적 스케줄 생성을 위한 필드
    private LocalDate workDate;
    private LocalTime startTime;

    private Long departmentId;
    private LocalDateTime appointmentDatetime;
    private String symptom;
    private String note;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
