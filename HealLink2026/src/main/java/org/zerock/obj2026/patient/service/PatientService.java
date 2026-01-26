package org.zerock.obj2026.patient.service;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.member.domain.User;
import org.zerock.obj2026.member.repository.UserRepository;
import org.zerock.obj2026.patient.domain.Patient;
import org.zerock.obj2026.patient.dto.PatientDTO;
import org.zerock.obj2026.patient.repository.PatientRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    // 환자 1명 정보 출력
    public PatientDTO getPatient(Long patientId){
        Optional<Patient> patient = patientRepository.findById(patientId);
        if(patient.isPresent()){
            return new PatientDTO(patient.get());
        }
        return new PatientDTO();
    }

    public PatientDTO addPatient(PatientDTO dto, String userId){
        Optional<User> user = userRepository.findByEmail(userId);
        Patient patient = dto.convertPatient(user.get());
        return new PatientDTO(patientRepository.save(patient));
    }

    @Transactional
    public void editPatient(PatientDTO dto, User user){
        Patient patient = patientRepository.findById(dto.getPatientId()).get();
        if(patient.getPatientId().equals(user.getUserId())){
            patient.changePatient(dto);
        }
    }

    public void removePatient(Long patientId){
        patientRepository.deleteById(patientId);
    }

}
