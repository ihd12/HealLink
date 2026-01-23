package org.zerock.obj2026.appointment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.appointment.dto.AppointmentDTO;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.department.domain.Department;
import org.zerock.obj2026.department.repository.DepartmentRepository;
import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.doctor.repository.DoctorRepository;
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
    private final DoctorRepository doctorRepository;

    @Override
    public Long createAppointment(AppointmentDTO dto) {
        log.info("Creating appointment: {}", dto);

        Long patientId = dto.getPatientId();
        Long departmentId = dto.getDepartmentId();

        //  ID 유효성 검사
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null (환자 정보가 없습니다)");
        }
        if (departmentId == null) {
            throw new IllegalArgumentException("Department ID cannot be null (진료과 정보가 누락되었습니다)");
        }
        
        // 동적 생성 시 의사 ID 체크
        if (dto.getScheduleId() == null && dto.getDoctorId() == null) {
             throw new IllegalArgumentException("Doctor ID cannot be null (의사 정보가 누락되었습니다)");
        }

        // 환자 및 진료과 조회
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID: " + patientId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department ID: " + departmentId));

        // 스케줄 확보
        DoctorSchedule schedule;

        if (dto.getScheduleId() != null) {
            // Case A: 스케줄 ID가 있는 경우
            schedule = doctorScheduleRepository.findById(dto.getScheduleId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID: " + dto.getScheduleId()));
        } else {
            // Case B: 스케줄 ID가 없는 경우
            if (dto.getDoctorId() == null || dto.getWorkDate() == null || dto.getStartTime() == null) {
                throw new IllegalArgumentException("스케줄 생성을 위한 필수 정보(의사, 날짜, 시간)가 누락되었습니다.");
            }

            schedule = doctorScheduleRepository.findByDoctorDoctorId(dto.getDoctorId()).stream()
                    .filter(s -> s.getWorkDate().equals(dto.getWorkDate()) && s.getStartTime().equals(dto.getStartTime()))
                    .findFirst()
                    .orElse(null);

            if (schedule == null) {
                // 없으면 새로 생성
                Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID: " + dto.getDoctorId()));

                java.time.LocalTime endTime = dto.getStartTime().plusMinutes(30);

                schedule = DoctorSchedule.builder()
                        .doctor(doctor)
                        .workDate(dto.getWorkDate())
                        .startTime(dto.getStartTime())
                        .endTime(endTime)
                        .isAvailable(true)
                        .build();
                
                schedule = doctorScheduleRepository.save(schedule);
                log.info("New schedule created dynamically: ID={}", schedule.getScheduleId());
            }
        }

        // 예약 가능 여부 확인
        if (!schedule.getIsAvailable()) {
            throw new IllegalStateException("이미 예약이 완료된 시간입니다.");
        }

        schedule.setIsAvailable(false); // 스케줄 마감 처리

    //예약 메인기능
        LocalDateTime appointmentDateTime = LocalDateTime.of(schedule.getWorkDate(), schedule.getStartTime());

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .schedule(schedule)
                .department(department)
                .appointmentDateTime(appointmentDateTime)
                .symptom(dto.getSymptom())
                .note(dto.getNote())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment created with ID: {}", savedAppointment.getAppointmentId());

        return savedAppointment.getAppointmentId();
    }
}
