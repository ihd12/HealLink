package org.zerock.obj2026.hospital.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
                                // 제네릭 E타입 부여
    public class HpageResponseDTO<E> {
        private HPageRequestDTO pageRequestDTO;
        private int totalPages;
        private long totalElements;
        private List<E> dtoList;

        private int page; // 현제 페이지
        private int size; // 출력 개수
        private int start; // 페이지 번호 시작값
        private int end; // 페이지 번호 끝값
        private boolean prev; // 이전버튼 여부
        private boolean next; // 다음버튼 여부
        public static <E> HpageResponseDTO<E> of(Page<E> page) {
            HpageResponseDTO<E> dto = new HpageResponseDTO<>();
            dto.totalPages = page.getTotalPages();
            dto.totalElements = page.getTotalElements();
            dto.dtoList = page.getContent();

            dto.page = page.getNumber() + 1;

            dto.size = page.getSize();

            dto.end = (int)(Math.ceil(dto.page / 10.0) * 10);
            dto.start = dto.end - 9;
            dto.end = Math.min(dto.end, dto.totalPages);
            dto.prev = dto.start > 1;
            dto.next = dto.totalElements > (long) dto.end * dto.size;

            return dto;
        }
    }

