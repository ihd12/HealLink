package org.zerock.obj2026.mypage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReservationDTO {
    private String doctorName;
    private String hospitalName;
    private String createdAt;
}
