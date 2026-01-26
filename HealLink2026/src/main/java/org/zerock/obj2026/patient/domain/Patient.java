package org.zerock.obj2026.patient.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.patient.dto.PatientDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString(exclude = "user") // Exclude user from toString to avoid recursion
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient implements Persistable<Long> {

    @Id
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "patient_id")
    private User user;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 3)
    private String bloodType;

    @Column(nullable = false)
    private Boolean hasAllergies = false;

    @Lob
    private String allergies;

    @Lob
    private String medicalHistory;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void changePatient(PatientDTO dto){
        if(dto.getPhone()!=null) this.phone = dto.getPhone();
        if(dto.getBirthDate()!=null) this.birthDate = dto.getBirthDate();
        if(dto.getGender()!=null) this.gender = dto.getGender();
        if(dto.getAddress()!=null) this.address = dto.getAddress();
        if(dto.getBloodType()!=null) this.bloodType = dto.getBloodType();
        if(dto.getHasAllergies()!=null) this.hasAllergies = dto.getHasAllergies();
        if(dto.getAllergies()!=null) this.allergies = dto.getAllergies();
        if(dto.getMedicalHistory()!=null) this.medicalHistory = dto.getMedicalHistory();
    }

    @Override
    public Long getId() {
        return this.patientId;
    }

    @Override
    public boolean isNew() {
        return this.createdAt == null;
    }
}
