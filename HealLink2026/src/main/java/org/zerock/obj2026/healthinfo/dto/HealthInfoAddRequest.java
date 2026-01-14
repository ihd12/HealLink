// 게시글 쓰기용 리퀘스트

package org.zerock.obj2026.healthinfo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerock.obj2026.healthinfo.domain.HealthInfo;
import org.zerock.obj2026.healthinfo.domain.HealthInfoCategory;
import org.zerock.obj2026.healthinfo.domain.HealthInfoSourceType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class HealthInfoAddRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content;

    private HealthInfoCategory category;
    private HealthInfoSourceType sourceType;

    public HealthInfo toEntity() {
        // content의 앞부분 100자만 떼어서 summary로 만들기 (예시 로직)
        String autoSummary = (content != null && content.length() > 100)
                ? content.substring(0, 100) + "..."
                : content;

        return HealthInfo.builder()
                .title(title)
                .content(content)
                .summary(autoSummary) // 여기서 자동으로 넣어줍니다.
                .category(category)
                .sourceType(sourceType)
                .build();
    }
}
