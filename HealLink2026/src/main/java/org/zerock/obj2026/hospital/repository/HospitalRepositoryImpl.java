package org.zerock.obj2026.hospital.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.zerock.obj2026.hospital.domain.Hospital;
import org.zerock.obj2026.hospital.domain.QHospital;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.obj2026.hospital.dto.HPageRequestDTO;

import java.util.List;

@Repository
public class HospitalRepositoryImpl
        extends QuerydslRepositorySupport
        implements HospitalSearch {

    public HospitalRepositoryImpl() {
        super(Hospital.class);
    }

    @Override
    public Page<Hospital> search(HPageRequestDTO requestDTO) {

        QHospital hospital = QHospital.hospital;
        JPQLQuery<Hospital> query = from(hospital);

        String[] types = requestDTO.splitTypes();
        String keyword = requestDTO.getKeyword();
        Pageable pageable = requestDTO.getPageable();
        String sido = requestDTO.getSido();
        String gu = requestDTO.getGu();
        String dong = requestDTO.getDong();

        if (types != null && types.length > 0 && StringUtils.hasText(keyword)) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "n": // 병원 이름
                        builder.or(hospital.dutyName.contains(keyword));
                        break;
                    case "a": // 주소
                        builder.or(hospital.dutyAddr.contains(keyword));
                        break;
                }
            }
            query.where(builder);
        }
        if (sido != null && !sido.isEmpty()||gu != null && !gu.isEmpty()||dong != null && !dong.isEmpty()) {

            query.where(hospital.sido.eq(sido));
            query.where(hospital.sigungu.eq(gu));
            query.where(hospital.emd.eq(dong));
        }
        query.where(hospital.hpid.isNotNull()); // 기본 필터

        getQuerydsl().applyPagination(pageable, query);

        List<Hospital> list = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(list, pageable, total);
    }

    // 지역 기반 검색
    @Override
    public Page<Hospital> findHospitalsByRegion(String sido, String gu, String dong, Pageable pageable) {

        QHospital hospital = QHospital.hospital;
        JPQLQuery<Hospital> query = from(hospital);

        BooleanBuilder builder = new BooleanBuilder();

        if (sido != null) {
            builder.and(hospital.dutyAddr.contains(sido));
        }
        if (gu != null) {
            builder.and(hospital.dutyAddr.contains(gu));
        }
        if (dong != null) {
            builder.and(hospital.dutyAddr.contains(dong));
        }

        query.where(builder);

        getQuerydsl().applyPagination(pageable, query);

        List<Hospital> list = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(list, pageable, total);
    }
}
