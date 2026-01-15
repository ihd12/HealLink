package org.zerock.obj2026.healthinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.obj2026.healthinfo.domain.HealthInfo;
import org.zerock.obj2026.healthinfo.domain.HealthInfoCategory;
import org.zerock.obj2026.healthinfo.domain.HealthInfoSourceType;

import java.util.List;

public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {

    // 전체 조회 (최신순)
    Page<HealthInfo> findAllByOrderByHealthInfoIdDesc(Pageable pageable);

    // 필터링 기능 (원래의 카테고리/출처 필터링)
    @Query("SELECT h FROM HealthInfo h WHERE " +
            "(:categories IS NULL OR h.category IN :categories) AND " +
            "(:sources IS NULL OR h.sourceType IN :sources) AND " +
            "(:keyword IS NULL OR (h.title LIKE CONCAT('%', :keyword, '%') " +
            "OR h.content LIKE CONCAT('%', :keyword, '%')))")
    Page<HealthInfo> findAllWithFilters(
            @Param("categories") List<HealthInfoCategory> categories,
            @Param("sources") List<HealthInfoSourceType> sources,
            @Param("keyword") String keyword,
            Pageable pageable);
}