package org.zerock.obj2026.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.obj2026.appointment.domain.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /* 마이페이지 내예약 목록 조회 */
    List<Appointment> findAllByPatientUserEmailOrderByCreatedAtDesc(String email);
}
