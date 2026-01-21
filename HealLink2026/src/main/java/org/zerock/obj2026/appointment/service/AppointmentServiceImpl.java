package org.zerock.obj2026.appointment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.department.domain.Department;
import org.zerock.obj2026.department.repository.DepartmentRepository;
import org.zerock.obj2026.doctor_schedule.domain.DoctorSchedule;
import org.zerock.obj2026.doctor_schedule.repository.DoctorScheduleRepository;
import org.zerock.obj2026.patient.domain.Patient;
import org.zerock.obj2026.patient.repository.PatientRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Long createAppointment(Long scheduleId, Long patientId, Long departmentId, String symptom, String note) {
        log.info("Creating appointment for schedule: {}, patient: {}, department: {}", scheduleId, patientId, departmentId);

        // 환자, 의사 스케줄, 진료과 엔티티를 조회 구간
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID: " + patientId));

        DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID: " + scheduleId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department ID: " + departmentId));

        if (!schedule.getIsAvailable()) {
            throw new IllegalStateException("이미 예약이 완료된 시간입니다.");
        }

        // 예약 마감
        schedule.setIsAvailable(false);


        LocalDateTime appointmentDateTime = LocalDateTime.of(schedule.getWorkDate(), schedule.getStartTime());

        // 예약 생성
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .schedule(schedule)
                .department(department)
                .appointmentDatetime(appointmentDateTime)
                .symptom(symptom)
                .note(note)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment created with ID: {}", savedAppointment.getAppointmentId());

        return savedAppointment.getAppointmentId();
    }
}
