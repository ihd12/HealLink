package org.zerock.obj2026.admin.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.admin.notice.dto.NoticeAddRequest;
import org.zerock.obj2026.admin.notice.service.NoticeService;

// Axios
@RestController
@RequiredArgsConstructor
// Axios에서 호출하는 공통 경로 : /api/admin/notice
@RequestMapping("/api/admin/notice")
public class NoticeApiController {
    private final NoticeService noticeService;

    // 읽기는 하지 않았음
    // 쓰기
    @PostMapping
    public ResponseEntity<Long> createNotice(@RequestBody NoticeAddRequest dto) {
        Long noticeId = noticeService.createNotice(dto);
        return ResponseEntity.ok(noticeId);
    }

    // 수정
    @PutMapping("/{id}")  // 위에 있으니까 "/api/admin/notice/{id}"라고 쓰면 중복됨
    public ResponseEntity<Long> updateNotice(@PathVariable Long id, @RequestBody NoticeAddRequest dto) {
        noticeService.updateNotice(id, dto);
        return ResponseEntity.ok(id);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }

}
