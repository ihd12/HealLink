package org.zerock.obj2026.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MypageDTO {

    /* 회원정보 조회용 */
    private String name;
    private String email;

    /* 회원정보 수정용 */
    private String tel;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    /* 예약 조회용 */
    private String doctorName;
    private String hospitalName;
    private String createdAt;

    /* 예약 상세 */
    private Long appointmentId;
}
