package org.zerock.obj2026.mypage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.member.repository.UserRepository;
import org.zerock.obj2026.mypage.dto.MypageDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /* 회원정보 수정 */
    @Transactional
    public void updateUserProfile(Long userId, MypageDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setTel(dto.getTel());

        if(dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            /* 현재 비밀번호 확인 */
            if (dto.getCurrentPassword() == null ||
                    !passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }
            /* 새 비밀번호 확인 */
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
            }
            /* 비밀번호 암호화 후 저장 */
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }
    }

    /* 회원정보 조회 */
    public MypageDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return MypageDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .tel(user.getTel())
                .build();
    }


    /* 예약 조회 */
    public Page<MypageDTO> getReservationsForUser(String email, Pageable pageable) {
        Page<Appointment> appointments =
                appointmentRepository
                        .findByPatientUserEmailOrderByCreatedAtDesc(email, pageable);

        return appointments.map(a -> {
                    Doctor doctor = a.getSchedule().getDoctor();
                    String hospitalName =
                            doctor.getHospital() != null
                                    ? doctor.getHospital().getDutyName()
                                    : "병원 정보 없음";
                    return MypageDTO.builder()
                            .appointmentId(a.getAppointmentId())
                            .hospitalName(hospitalName)
                            .doctorName(doctor.getUser().getName()) // 유저 이름으로 표시
                            .createdAt(a.getCreatedAt().toString())
                            .build();
                });
    }

    // 예약 상세 (페이지용)
    public MypageDTO getReservationDetail(Long appointmentId) {
        // Optional 안전 처리
        Appointment a = appointmentRepository.findById(appointmentId).orElse(null);
        if (a == null) return null;  // 예약 없으면 null 반환

        Doctor d = a.getSchedule().getDoctor();
        String hospitalName = d.getHospital() != null ? d.getHospital().getDutyName() : "병원 정보 없음";

        return MypageDTO.builder()
                .appointmentId(a.getAppointmentId())
                .hospitalName(hospitalName)
                .doctorName(d.getUser().getName())
                .createdAt(a.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .email(a.getPatient().getUser().getEmail())
                .build();
    }

    public MypageDTO getReservationByIdAndEmail(Long appointmentId, String email) {
        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElse(null);

        if (appointment == null) return null;
        if (!appointment.getPatient().getUser().getEmail().equals(email)) return null;

        Doctor doctor = appointment.getSchedule().getDoctor();
        String hospitalName = doctor.getHospital() != null ? doctor.getHospital().getDutyName() : "병원 정보 없음";

        return MypageDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .hospitalName(hospitalName)
                .doctorName(doctor.getUser().getName())
                .createdAt(appointment.getCreatedAt().toString())
                .build();
    }
}
