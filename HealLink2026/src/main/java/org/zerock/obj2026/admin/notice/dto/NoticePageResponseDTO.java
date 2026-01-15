package org.zerock.obj2026.admin.notice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class NoticePageResponseDTO<E> {

    private int page;
    private int size;
    private int total;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private List<E> dtoList;

    // 1. [추가] 모든 페이지 상단에 노출할 고정글 리스트
    private List<E> pinnedList;

    // [중요] 이 필드가 있어야 타임리프에서 검색 조건을 꺼내 쓸 수 있습니다!
    private NoticePageRequestDTO pageRequestDTO;

    @Builder(builderMethodName = "withAll")
    public NoticePageResponseDTO(NoticePageRequestDTO pageRequestDTO, List<E> dtoList,
                                 List<E> pinnedList, int total) {
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.pinnedList = pinnedList; // 고정글

        // [중요] 넘겨받은 객체를 필드에 저장해야 합니다!
        this.pageRequestDTO = pageRequestDTO;

        // 끝 번호 계산
        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;
        this.start = this.end - 9;

        // 진짜 마지막 페이지 번호 계산
        int last = (int)(Math.ceil((total / (double)size)));
        this.end = end > last ? last : end;

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}