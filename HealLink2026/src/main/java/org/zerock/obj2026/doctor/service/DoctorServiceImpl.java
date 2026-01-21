package org.zerock.obj2026.doctor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.obj2026.department.dto.DepartmentDTO;
import org.zerock.obj2026.doctor.domain.Doctor;
import org.zerock.obj2026.doctor.dto.DoctorDTO;
import org.zerock.obj2026.doctor.repository.DoctorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DoctorDTO> getDoctorsByHospital(String hpid) {
        List<Doctor> doctors = doctorRepository.findByHospitalHpid(hpid);

        return doctors.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO getDoctorDTO(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 의사입니다. ID=" + doctorId));
        return entityToDTO(doctor);
    }

    private DoctorDTO entityToDTO(Doctor doctor) {
        DoctorDTO dto = modelMapper.map(doctor, DoctorDTO.class);

        dto.setName(doctor.getUser().getName());
        if (doctor.getHospital() != null) {
            dto.setHospitalName(doctor.getHospital().getDutyName());
            dto.setHospitalId(doctor.getHospital().getHpid()); // ID 매핑 복구
        }

        dto.setDepartments(doctor.getDepartments().stream()
                .map(dept -> modelMapper.map(dept, DepartmentDTO.class))
                .collect(Collectors.toSet()));

        return dto;
    }
}
