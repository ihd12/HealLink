package org.zerock.obj2026.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.obj2026.doctor.domain.Doctor;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByHospitalHpid(String hpid);
}
