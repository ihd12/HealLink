package org.zerock.obj2026.doctor_schedule.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.obj2026.doctor_schedule.domain.DoctorSchedule;
import org.zerock.obj2026.doctor_schedule.dto.DoctorScheduleDTO;
import org.zerock.obj2026.doctor_schedule.repository.DoctorScheduleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.doctor.repository.DoctorRepository;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.appointment.domain.Appointment;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository; // 추가
    private final ModelMapper modelMapper;

    @Override
    public DoctorScheduleDTO register(DoctorScheduleDTO doctorScheduleDTO) {
        DoctorSchedule doctorSchedule = modelMapper.map(doctorScheduleDTO, DoctorSchedule.class);
        
        // doctorId를 사용하여 Doctor 조회 및 설정
        if (doctorScheduleDTO.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(doctorScheduleDTO.getDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID: " + doctorScheduleDTO.getDoctorId()));
            doctorSchedule.setDoctor(doctor);
        }

        DoctorSchedule savedSchedule = doctorScheduleRepository.save(doctorSchedule);
        return modelMapper.map(savedSchedule, DoctorScheduleDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorScheduleDTO> getScheduleByDoctor(Long doctorId) {
        // 스케줄 목록 조회
        List<DoctorSchedule> scheduleList = doctorScheduleRepository.findByDoctorDoctorId(doctorId);
        
        //예약 목록 조회
        List<Appointment> appointmentList = appointmentRepository.findBySchedule_Doctor_DoctorId(doctorId);
        Map<Long, Appointment> appointmentMap = appointmentList.stream()
                .collect(Collectors.toMap(
                        appt -> appt.getSchedule().getScheduleId(),
                        Function.identity(),
                        (existing, replacement) -> existing // 중복 키 발생 시 기존 값 유지
                ));

        return scheduleList.stream()
                .map(doctorSchedule -> {
                    DoctorScheduleDTO dto = modelMapper.map(doctorSchedule, DoctorScheduleDTO.class);
                    // DTO에 doctorId 명시적 설정
                    if (doctorSchedule.getDoctor() != null) {
                        dto.setDoctorId(doctorSchedule.getDoctor().getDoctorId());
                    }
                    
                    // 예약 정보 매핑
                    if (appointmentMap.containsKey(doctorSchedule.getScheduleId())) {
                        Appointment appointment = appointmentMap.get(doctorSchedule.getScheduleId());
                        if (appointment.getPatient() != null && appointment.getPatient().getUser() != null) {
                            dto.setPatientName(appointment.getPatient().getUser().getName()); 
                        }
                        dto.setSymptom(appointment.getSymptom());
                        // 예약이 있으면 available = false
                        dto.setIsAvailable(false); 
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorScheduleDTO getSchedule(Long scheduleId) {
        Optional<DoctorSchedule> result = doctorScheduleRepository.findById(scheduleId);
        return result.map(doctorSchedule -> modelMapper.map(doctorSchedule, DoctorScheduleDTO.class))
                .orElse(null);
    }

    @Override
    public DoctorScheduleDTO modify(Long scheduleId, DoctorScheduleDTO doctorScheduleDTO) {
        Optional<DoctorSchedule> result = doctorScheduleRepository.findById(scheduleId);
        if (result.isPresent()) {
            DoctorSchedule doctorSchedule = result.get();
            if (doctorScheduleDTO.getWorkDate() != null) {
                doctorSchedule.setWorkDate(doctorScheduleDTO.getWorkDate());
            }
            if (doctorScheduleDTO.getStartTime() != null) {
                doctorSchedule.setStartTime(doctorScheduleDTO.getStartTime());
            }
            if (doctorScheduleDTO.getEndTime() != null) {
                doctorSchedule.setEndTime(doctorScheduleDTO.getEndTime());
            }
            if (doctorScheduleDTO.getIsAvailable() != null) {
                doctorSchedule.setIsAvailable(doctorScheduleDTO.getIsAvailable());
            }
// updatedSchedule 변수 선언 후 초기화
            DoctorSchedule updatedSchedule = doctorScheduleRepository.save(doctorSchedule);
            return modelMapper.map(updatedSchedule, DoctorScheduleDTO.class);
        }
        return null;
    }

    @Override
    public void remove(Long scheduleId) {
        doctorScheduleRepository.deleteById(scheduleId);
    }
}
