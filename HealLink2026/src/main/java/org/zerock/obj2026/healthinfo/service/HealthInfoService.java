package org.zerock.obj2026.healthinfo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zerock.obj2026.healthinfo.domain.HealthInfo;
import org.zerock.obj2026.healthinfo.dto.HealthInfoAddRequest;
import org.zerock.obj2026.healthinfo.dto.HealthInfoResponse;
import org.zerock.obj2026.healthinfo.repository.HealthInfoRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthInfoService {
    private final HealthInfoRepository healthInfoRepository;

    // 1. 목록 .findAll...
    public List<HealthInfoResponse> findAll(){
        return healthInfoRepository.findAllByOrderByHealthInfoIdDesc().stream().map(HealthInfoResponse::new).toList();
    }

    // 2. 상세 조회  .findById
    public HealthInfoResponse findById(long id){
        HealthInfo healthInfo =  healthInfoRepository.findById(id)
                        // 예외처리
                        .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new HealthInfoResponse(healthInfo);
    }

    // 3. 쓰기
    @Transactional
    public Long createPost(HealthInfoAddRequest dto) {
        HealthInfo entity = dto.toEntity();
        return healthInfoRepository.save(entity).getHealthInfoId();
    }

    // 4. 지우기
    @Transactional
    public void delete(Long id) {
        // 해당 ID의 게시글이 있는지 먼저 확인 (안전장치)
        HealthInfo healthInfo = healthInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // 삭제 실행
        healthInfoRepository.delete(healthInfo);
    }

    // 5. 수정
    @Transactional
    public Long update(Long id, HealthInfoAddRequest dto) {
        HealthInfo healthInfo = healthInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // 엔티티의 메서드(.update)를 통해 데이터 변경
        healthInfo.update(
                dto.getTitle(),
                dto.getContent(),
                dto.getCategory(),  // DTO에 있는 카테고리
                dto.getSourceType() // DTO에 있는 출처타입
        );
        return id;
    }


}