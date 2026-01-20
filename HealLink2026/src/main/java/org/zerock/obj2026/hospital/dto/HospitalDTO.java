package org.zerock.obj2026.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDTO {
    private String hpid;
    private String dutyName;
    private String dutyAddr;
    
    private String sido;
    private String sigungu;
    private String emd;

    private String dutyTel1;
    private String dutyTel3; // 응급실 전화
    
    private String dutyDiv;
    private String dutyDivNam; // 분류명
    private String dutyEmcls;
    private String dutyEmclsName;
    
    private String dutyInf; // 병원 정보
    private String dutyEtc; // 기타 정보
    private String dutyMapimg;
    
    private BigDecimal wgs84Lat;
    private BigDecimal wgs84Lon;
    
    private Integer dutyTime1s;
    private Integer dutyTime1c;
    private Integer dutyTime2s;
    private Integer dutyTime2c;
    private Integer dutyTime3s;
    private Integer dutyTime3c;
    private Integer dutyTime4s;
    private Integer dutyTime4c;
    private Integer dutyTime5s;
    private Integer dutyTime5c;
    private Integer dutyTime6s;
    private Integer dutyTime6c;
    private Integer dutyTime7s;
    private Integer dutyTime7c;
    private Integer dutyTime8s;
    private Integer dutyTime8c;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}