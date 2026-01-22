package org.zerock.obj2026.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.patient.domain.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class PatientDTO {
    private Long patientId; // References UserDTO's userId
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String bloodType;
    private Boolean hasAllergies = false; // TINYINT(1) can map to Boolean
    private String allergies;
    private String medicalHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public PatientDTO(Patient patient){
        this.patientId =  patient.getPatientId();
        this.phone = patient.getPhone();
        this.birthDate = patient.getBirthDate();
        this.gender = patient.getGender();
        this.address = patient.getAddress();
        this.bloodType = patient.getBloodType();
        this.hasAllergies = patient.getHasAllergies();
        this.allergies = patient.getAllergies();
        this.medicalHistory = patient.getMedicalHistory();
        this.createdAt = patient.getCreatedAt();
        this.updatedAt = patient.getUpdatedAt();
    }
    public Patient convertPatient(User user){
        return Patient.builder()
                .patientId(user.getUserId())
                .user(user)
                .phone(user.getTel())
                .birthDate(this.getBirthDate())
                .gender(this.getGender())
                .address(this.getAddress())
                .bloodType(this.getBloodType())
                .hasAllergies(this.getHasAllergies())
                .allergies(this.getAllergies())
                .medicalHistory(this.getMedicalHistory())
                .build();
    }
}
