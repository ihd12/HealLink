package org.zerock.obj2026.doctor_schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DoctorScheduleDTO {
    private Long scheduleId;
    private Long doctorId; // doctor DTO의 doctorId 참조
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 예약 정보
    private String patientName;
    private String symptom;
}
