// 이것은 게시글 목록 불러오기용 리스폰스

package org.zerock.obj2026.healthinfo.dto;

import lombok.Getter;
import org.zerock.obj2026.healthinfo.domain.HealthInfo;

import java.time.LocalDateTime;


@Getter
public class HealthInfoResponse {
    private final Long id;
    private final String title;
    private final String summary;
    private final String content;
    private final String category;
    private final String sourceType; // 추가
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public HealthInfoResponse(HealthInfo healthInfo) {
        this.id = healthInfo.getHealthInfoId();
        this.title = healthInfo.getTitle();
        this.summary = healthInfo.getSummary();
        this.content = healthInfo.getContent();

        // Enum 처리 (null 방어 코드 포함)
        this.category = (healthInfo.getCategory() != null) ? healthInfo.getCategory().name() : null;
        this.sourceType = (healthInfo.getSourceType() != null) ? healthInfo.getSourceType().name() : null;
        this.createdAt = healthInfo.getCreatedAt();
        this.updatedAt = healthInfo.getUpdatedAt();

    }
}
