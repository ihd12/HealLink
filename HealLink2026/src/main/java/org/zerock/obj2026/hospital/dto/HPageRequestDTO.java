package org.zerock.obj2026.hospital.dto;

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
public class HPageRequestDTO {
    @Builder.Default
    private int page = 1; // 페이지 번호
    @Builder.Default
    private int size = 10; // 출력 개수
    private String types; // 검색 조건
    private String keyword; // 검색어
    private String link; // 페이지,사이즈,검색조건등을 url맞게 반환하는 변수
    private String sido; // 시/도
    private String gu; // 시/군/구
    private String dong; // 읍/면/동
    public Pageable getPageable() {
        return PageRequest.of(page-1, size, Sort.by("hpid").descending());
    }
    public String[] splitTypes(){
        if( types == null || types.length()==0){
            return null;
        }
        return types.split("");
    }
    public String getLink() {
        StringBuilder builder = new StringBuilder();
        builder.append("page=" + this.page);
        builder.append("&size=" + this.size);
        if (types != null && splitTypes().length > 0) {
            builder.append("&type=" +types);
        }
        if (keyword != null) {
            try {
                builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sido != null && !sido.isEmpty()) {
            builder.append("&sido=" +sido);
        }
        if (gu != null && !gu.isEmpty()) {
            builder.append("&gu=" +gu);
        }
        if (dong != null && !dong.isEmpty()) {
            builder.append("&dong=" +dong);
        }

        return builder.toString();
    }
}
