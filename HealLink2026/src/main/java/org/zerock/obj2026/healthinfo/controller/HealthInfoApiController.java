package org.zerock.obj2026.healthinfo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.admin.notice.dto.NoticePageRequestDTO;
import org.zerock.obj2026.admin.notice.dto.NoticePageResponseDTO;
import org.zerock.obj2026.healthinfo.dto.HealthInfoAddRequest;
import org.zerock.obj2026.healthinfo.dto.HealthInfoResponse;
import org.zerock.obj2026.healthinfo.service.HealthInfoService;

import java.util.List;

@RestController
@RequiredArgsConstructor

// Axios
public class HealthInfoApiController
{
    private final HealthInfoService healthInfoService;

    // 1. 목록 + 페이징 필터링(2026-01-15)
    @GetMapping("/api/healthinfo")
    public ResponseEntity<Page<HealthInfoResponse>> findAllHealthInfo(
            // 스프링 표준 Pageable 사용 (0페이지부터 시작, 기본 10개씩 최신순)
            @PageableDefault(page = 0, size = 10, sort = "healthInfoId", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> sources,
            @RequestParam(required = false) String keyword)
    {
        // 서비스에 표준 Pageable 전달
        Page<HealthInfoResponse> response = healthInfoService.findAllWithFilters(
                pageable, categories, sources, keyword);

        return ResponseEntity.ok(response);
    }


    // 2. 쓰기
    @PostMapping("/api/healthinfo")
    public ResponseEntity<?> create(@Valid @RequestBody HealthInfoAddRequest dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("입력값이 올바르지 않습니다.");
        }
        Long savedId = healthInfoService.createPost(dto);
        return ResponseEntity.ok().body(savedId + "번 등록 성공");
    }

    // 3. 수정
    @PutMapping("/api/healthinfo/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody HealthInfoAddRequest dto) {
        Long updatedId = healthInfoService.update(id, dto);
        return ResponseEntity.ok().body(updatedId);
    }

    // 4. 삭제
    @DeleteMapping("/api/healthinfo/{id}")
    public ResponseEntity<Void> deleteHealthInfo(@PathVariable Long id) {
        healthInfoService.delete(id); // 서비스에서 deleteById 실행
        return ResponseEntity.ok().build();
    }


}
