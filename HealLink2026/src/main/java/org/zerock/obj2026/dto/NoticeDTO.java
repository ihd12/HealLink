package org.zerock.obj2026.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.obj2026.domain.Notice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NoticeDTO {
    private Long noticeId;
    private String title;
    private String content;
    private Long writerId; // References UserDTO's userId
    private Integer viewCount;
    private Boolean isPinned; // TINYINT(1) can map to Boolean
    private Boolean isDeleted; // TINYINT(1) can map to Boolean
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NoticeDTO(Notice notice){
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.writerId = notice.getWriter().getUserId();
        this.viewCount = notice.getViewCount();
        this.isPinned = notice.getIsPinned();
        this.isDeleted = notice.getIsDeleted();
        this.createdAt = notice.getCreatedAt();
        this.updatedAt = notice.getUpdatedAt();
    }
}
