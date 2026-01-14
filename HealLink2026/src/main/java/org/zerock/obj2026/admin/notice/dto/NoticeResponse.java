package org.zerock.obj2026.admin.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.admin.notice.domain.Notice;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String content;
    private Long writerId;
    private String writerName;
    private Integer viewCount;  // 조회수
    private Boolean isPinned;   // 상단 고정 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 롬복(@Data)이 Getter/Setter를 자동 생성해주므로, this.를적지 않고도 객체의 데이터 사용 가능

    // fromEntity
    public static NoticeResponse fromEntity(Notice notice) {
        return NoticeResponse.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writerId(notice.getWriter().getUserId()) // User 엔티티에서 ID 추출
                .writerName(notice.getWriter().getName()) // User 엔티티에서 이름 추출
                .viewCount(notice.getViewCount())
                .isPinned(notice.getIsPinned())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }

}
