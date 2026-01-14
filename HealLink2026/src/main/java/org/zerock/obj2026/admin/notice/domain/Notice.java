package org.zerock.obj2026.admin.notice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.zerock.obj2026.member.domain.User;

import java.time.LocalDateTime;
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString(exclude = "writer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert // To apply @ColumnDefault on insert
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer viewCount;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isPinned;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 조회수 증가 .incrementViewCount
    public void incrementViewCount() {
        this.viewCount++;
    }

    // 수정용 .update
    public void update(String title, String content, boolean isPinned) {
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
    }

    // 삭제용(소프트 삭제) .delete
    public void delete() {
        this.isDeleted = true;
    }

}
