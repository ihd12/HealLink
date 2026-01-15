package org.zerock.obj2026.admin.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticePageRequestDTO {
    @Builder.Default
    private int page = 1; // 페이지 번호
    @Builder.Default
    private int size = 10; // 출력 개수
    private String types; // 검색 조건
    private String keyword; // 검색어
    private String link; // 페이지,사이즈,검색조건등을 url맞게 반환하는 변수

    // Pageable 생성 메서드 : [고정글 맨 위로 가게] 추가
    public Pageable getPageable() {
        // 1순위: 고정 여부(isPinned) 내림차순 (true가 위로)
        // 2순위: ID(noticeId) 내림차순 (최신글이 위로)
        return PageRequest.of(page - 1, size,
                Sort.by("isPinned").descending()
                        .and(Sort.by("noticeId").descending()));
    }

    public String[] splitTypes(){
        if( types == null || types.length()==0){
            return null;
        }
        return types.split("");
    }

    public String getLink(int pageNum) { // (int pageNum) 추가하니까 드디어 되네요 2026-01-15
        StringBuilder builder = new StringBuilder();
        builder.append("?page=").append(pageNum); // 받은 번호를 사용
        builder.append("&size=").append(this.size);

        if (types != null && !types.isEmpty()) {
            builder.append("&types=").append(this.types);
        }
        if (keyword != null && !keyword.isEmpty()) {
            try {
                builder.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

}