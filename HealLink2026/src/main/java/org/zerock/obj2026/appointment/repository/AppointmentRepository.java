package org.zerock.obj2026.appointment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.obj2026.appointment.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /* 마이페이지 내예약 목록 조회 */
    Page<Appointment> findByPatientUserEmailOrderByAppointmentDateTimeDesc(
            String email,
            Pageable pageable
    );
}
