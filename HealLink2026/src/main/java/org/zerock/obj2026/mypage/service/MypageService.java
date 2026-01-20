package org.zerock.obj2026.mypage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.appointment.repository.AppointmentRepository;
import org.zerock.obj2026.department.domain.Department;
import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.doctor_schedule.domain.DoctorSchedule;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.member.repository.UserRepository;
import org.zerock.obj2026.mypage.dto.MypageDTO;

import java.time.format.DateTimeFormatter;

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

    // 회원 탈퇴
    @Transactional
    public void withdrawUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /* 예약 조회 */
    public Page<MypageDTO> getReservationsForUser(String email, Pageable pageable) {
        Page<Appointment> appointments =
                appointmentRepository
                        .findByPatientUserEmailOrderByAppointmentDateTimeDesc(email, pageable);

        return appointments.map(a -> {
                    DoctorSchedule schedule = a.getSchedule();
                    Doctor doctor = schedule.getDoctor();

                    String hospitalName =
                            doctor.getHospital() != null
                                    ? doctor.getHospital().getDutyName()
                                    : "병원 정보 없음";
                    String appointmentDate = "-";
                    String appointmentTime = "-";

                    if (a.getAppointmentDateTime() != null) {
                        appointmentDate =
                                a.getAppointmentDateTime().format(DATE_FORMATTER);
                        appointmentTime =
                                a.getAppointmentDateTime().format(TIME_FORMATTER);
                    }

                    return MypageDTO.builder()
                            .appointmentId(a.getAppointmentId())
                            .hospitalName(hospitalName)
                            .doctorName(doctor.getUser().getName())
                            .appointmentDate(appointmentDate)
                            .appointmentTime(appointmentTime)
                            .build();
                });
    }

    // 예약 상세 (페이지용)
    public MypageDTO getReservationDetail(Long appointmentId) {
        // Optional 안전 처리
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) return null;  // 예약 없으면 null 반환

        Doctor d = appointment.getSchedule().getDoctor();
        String hospitalName =
                d.getHospital() != null
                        ? d.getHospital().getDutyName()
                        : "병원 정보 없음";

        String departmentName = "진료과 정보 없음";

        if (d.getDepartments() != null && !d.getDepartments().isEmpty()) {
            departmentName = d.getDepartments()
                    .stream()
                    .map(Department::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("진료과 정보 없음");
        }

        return MypageDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .hospitalName(hospitalName)
                .doctorName(d.getUser().getName())
                .departmentName(departmentName)
                .appointmentDate(appointment.getAppointmentDateTime().format(DATE_FORMATTER))
                .appointmentTime(appointment.getAppointmentDateTime().format(TIME_FORMATTER))
                .build();
    }

    public MypageDTO getReservationByIdAndEmail(Long appointmentId, String email) {
        Appointment a = appointmentRepository
                .findById(appointmentId)
                .orElse(null);

        if (a == null) return null;
        if (!a.getPatient().getUser().getEmail().equals(email)) return null;

        Doctor doctor = a.getSchedule().getDoctor();
        String hospitalName =
                doctor.getHospital() != null
                        ? doctor.getHospital().getDutyName()
                        : "병원 정보 없음";

        return MypageDTO.builder()
                .appointmentId(a.getAppointmentId())
                .hospitalName(hospitalName)
                .doctorName(doctor.getUser().getName())
                .appointmentDate(
                        a.getAppointmentDateTime().format(DATE_FORMATTER)
                )
                .appointmentTime(
                        a.getAppointmentDateTime().format(TIME_FORMATTER)
                )
                .build();
    }
}
