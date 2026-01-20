package org.zerock.obj2026.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class MypageDTO {

    /* 회원정보 페이지 조회용 */
    private String name;
    private String email;

    /* 회원정보 페이지 수정용 */
    private String tel;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    /* 내 예약 조회용 */
    private Long appointmentId;
    private String doctorName;
    private String hospitalName;
    private String departmentName;

    /* 내 예약 상세 */
    private String appointmentDate;
    private String appointmentTime;
}
