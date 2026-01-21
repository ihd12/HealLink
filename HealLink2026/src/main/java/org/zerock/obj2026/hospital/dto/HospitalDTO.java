package org.zerock.obj2026.hospital.dto;

import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;



import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.List;



@Data

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class HospitalDTO {

    private String hpid; // 병원ID

    private String dutyName; // 병원이름

    private String dutyAddr; // 병원 주소



    private String sido; // 시/도

    private String sigungu;// 시/군/구

    private String emd;// 읍/면/동



    private String dutyTel1; // 병원 전화

    private String dutyTel3; // 응급실 전화



    private String dutyDiv; // 병원 분류

    private String dutyDivNam; // 분류명

    private String dutyEmcls; // 응급의료기관코드

    private String dutyEmclsName; // 응급의료기관코드명



    private String dutyInf; // 기관설명상세

    private String dutyEtc; // 비고

    private String dutyMapimg;// 간이약도



    private BigDecimal wgs84Lat;//병원위도

    private BigDecimal wgs84Lon;//병원경도



    private Integer dutyTime1s; // 1 2 << 숫자는 월~일 휴무 s 오픈시간 c 닫는시간

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

    // 운영시간
    private String monTime; //월 ~ 일 휴무

    private String tueTime;

    private String wedTime;

    private String thuTime;

    private String friTime;

    private String satTime;

    private String sunTime;

    private String holidayTime;


    //  병원소개 랜덤값
    private Double rating;

    private Integer reviewCount;

    private List<String> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}