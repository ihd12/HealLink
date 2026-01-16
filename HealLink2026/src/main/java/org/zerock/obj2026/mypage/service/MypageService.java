package org.zerock.obj2026.mypage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.member.repository.UserRepository;
import org.zerock.obj2026.mypage.dto.MypageDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /* 회원정보 수정 */
    @Transactional
    public void updateUserProfile(Long userId, MypageDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (dto.getTel() != null && !dto.getTel().isEmpty()) {
            user.setTel(dto.getTel());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
    }

    /* 회원정보 조회 */
    public MypageDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return MypageDTO.builder()
                .tel(user.getTel())
                .password("") // 비밀번호는 화면에 표시하지 않음
                .doctorName("") // 조회용, 필요 시 다른 서비스 연결
                .hospitalName("")
                .createdAt(user.getCreatedAt().toString())
                .build();
    }


    /* 예약 조회 */
    public List<MypageDTO> getReservationsForUser(String email) {
        List<Appointment> appointments =
                appointmentRepository.findAllByPatientUserEmailOrderByCreatedAtDesc(email);

        return appointments.stream()
                .map(a -> {
                    Doctor doctor = a.getSchedule().getDoctor();
                    String hospitalName = doctor.getHospital() != null ? doctor.getHospital().getDutyName() : "병원 정보 없음";
                    return MypageDTO.builder()
                            .hospitalName(hospitalName)
                            .doctorName(doctor.getUser().getName()) // 유저 이름으로 표시
                            .createdAt(a.getCreatedAt().toString())
                            .build();
                })
                .toList();
    }
}
