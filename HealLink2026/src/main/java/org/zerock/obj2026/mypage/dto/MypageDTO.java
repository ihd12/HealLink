package org.zerock.obj2026.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MypageDTO {

    /* 회원정보 수정용 */
    private String tel;
    private String password;

    /* 예약 조회용 */
    private String doctorName;
    private String hospitalName;
    private String createdAt;
}
