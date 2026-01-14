package org.zerock.obj2026.healthinfo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "health_info")
public class HealthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_info_id")
    private Long healthInfoId;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String summary;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthInfoCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthInfoSourceType sourceType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 수정용
    public void update(String title, String content, HealthInfoCategory category, HealthInfoSourceType sourceType) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.sourceType = sourceType;
    }
}
