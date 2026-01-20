package org.zerock.obj2026.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.obj2026.hospital.domain.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, String>, HospitalSearch {
}