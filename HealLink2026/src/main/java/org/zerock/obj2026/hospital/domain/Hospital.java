package org.zerock.obj2026.hospital.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    private String hpid;

    @Column(nullable = false, length = 100)
    private String dutyName;

    @Column(length = 255)
    private String dutyAddr;

    @Column(length = 50)
    private String sido;

    @Column(length = 50)
    private String sigungu;

    @Column(length = 50)
    private String emd;

    @Column(length = 20)
    private String dutyTel1;

    @Column(length = 20)
    private String dutyTel3;

    @Column(length = 20)
    private String dutyDiv;

    @Column(length = 100)
    private String dutyDivNam;

    @Column(length = 20)
    private String dutyEmcls;

    @Column(length = 100)
    private String dutyEmclsName;

    @Column(columnDefinition = "TEXT")
    private String dutyInf;

    @Column(columnDefinition = "TEXT")
    private String dutyEtc;

    @Column(length = 255)
    private String dutyMapimg;

    @Column(length = 1)
    private String dutyEryn;

    @Column(length = 10)
    private String postCdn1;

    @Column(length = 10)
    private String postCdn2;

    @Column(precision = 10, scale = 7)
    private BigDecimal wgs84Lat;

    @Column(precision = 10, scale = 7)
    private BigDecimal wgs84Lon;

    private Integer rnum;
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void configureRegionInfo(String sido, String sigungu, String emd) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.emd = emd;
    }
}