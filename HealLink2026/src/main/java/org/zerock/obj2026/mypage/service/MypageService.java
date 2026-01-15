package org.zerock.obj2026.mypage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.mypage.dto.MyReservationDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final AppointmentRepository appointmentRepository;

    public List<MyReservationDTO> getReservationsForUser(String email) {
        List<Appointment> appointments =
                appointmentRepository.findAllByPatientUserEmailOrderByCreatedAtDesc(email);

        return appointments.stream()
                .map(a -> {
                    Doctor doctor = a.getSchedule().getDoctor();
                    String hospitalName = doctor.getHospital() != null ? doctor.getHospital().getDutyName() : "병원 정보 없음";
                    return MyReservationDTO.builder()
                            .hospitalName(hospitalName)
                            .doctorName(doctor.getUser().getName()) // 유저 이름으로 표시
                            .createdAt(a.getCreatedAt().toString())
                            .build();
                })
                .toList();
    }
}
