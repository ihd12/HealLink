package org.zerock.obj2026.admin.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerock.obj2026.admin.notice.domain.Notice;
import org.zerock.obj2026.member.domain.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoticeAddRequest {
        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "내용은 필수 항목입니다.")
        private String content;

        private Long writerId;
        private Boolean isPinned;

        // healthinfo게시판과 다른 점 : 외부(Service)에서 조회한 User 객체를 주입받도록 설계
        public Notice toEntity(User writer) {
            return Notice.builder()
                    .title(this.title)
                    .content(this.content)
                    .writer(writer) // 이제 객체(User) 대 객체(User)로 타입이 맞습니다!
                    .isPinned(this.isPinned != null && this.isPinned)
                    .build();
        }
}
