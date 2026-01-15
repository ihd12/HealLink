package org.zerock.obj2026.admin.notice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.admin.notice.dto.NoticeAddRequest;
import org.zerock.obj2026.admin.notice.service.NoticeService;
import org.zerock.obj2026.member.dto.UserSecurityDTO;

@RestController
@RequiredArgsConstructor
// Axios에서 호출하는 공통 경로 : /api/admin/notice
@RequestMapping("/api/admin/notice")
public class NoticeApiController {
    private final NoticeService noticeService;

    // 1. 목록 x -> Axios 미사용

    // 2. 쓰기
    @PostMapping
    public ResponseEntity<Long> createNotice(@RequestBody NoticeAddRequest dto,
                                             @AuthenticationPrincipal UserSecurityDTO userDetails) { // 로그인확인으로 UserSecurityDTO사용
        Long noticeId = noticeService.createNotice(dto, userDetails.getUsername());
        return ResponseEntity.ok(noticeId);
    }

    // 3. 수정
    @PutMapping("/{id}")  //
        public ResponseEntity<Long> updateNotice(@PathVariable Long id, @RequestBody NoticeAddRequest dto,
                                             @AuthenticationPrincipal UserSecurityDTO userDetails) {
        noticeService.updateNotice(id, dto, userDetails.getUser().getUserId());
        return ResponseEntity.ok(id);
    }

    // 4. 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id,
                                             @AuthenticationPrincipal UserSecurityDTO userDetails) {
        noticeService.deleteNotice(id, userDetails.getUser().getUserId());
        return ResponseEntity.ok().build();
    }

}