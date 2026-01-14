package org.zerock.obj2026.healthinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.obj2026.healthinfo.domain.HealthInfo;

import java.util.List;

public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {
    List<HealthInfo> findAllByOrderByHealthInfoIdDesc();
}
