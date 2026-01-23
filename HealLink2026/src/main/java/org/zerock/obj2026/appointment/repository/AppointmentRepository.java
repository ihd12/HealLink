package org.zerock.obj2026.appointment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.obj2026.appointment.domain.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // 의사 ID로 예약 목록 조회 (스케줄 매핑용)
    List<Appointment> findBySchedule_Doctor_DoctorId(Long doctorId);

    /* 마이페이지 내예약 목록 조회 */
    Page<Appointment> findByPatientUserEmailOrderByAppointmentDateTimeDesc(
            String email,
            Pageable pageable
    );
}
