package org.zerock.obj2026.admin.notice.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.zerock.obj2026.admin.notice.dto.NoticeResponse;
import org.zerock.obj2026.admin.notice.service.NoticeService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class NoticeViewController {
    private final NoticeService noticeService;

    // 목록
    @GetMapping("/admin/notice")
    public String getAllNotice(Model model) {
        List<NoticeResponse> noticeList = noticeService.findAll();
        model.addAttribute("noticeList", noticeList);
        return "admin/notice";
    }

    // 읽기
    @GetMapping("/admin/notice/{id}")
    public String noticeDetail(@PathVariable("id") Long id, Model model) {
        NoticeResponse notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "admin/notice_detail";
    }

    // 쓰기
    @GetMapping("/admin/notice/create")
    public String createNoticePage() {
        // 리턴하는 문자열은 html 파일의 이름입니다 (notice_create.html)
        // 만약 폴더 안에 있다면 "admin/notice_create" 식으로 경로를 적어주세요.
        return "admin/notice_create";
    }

    // 수정
    @GetMapping("/admin/notice/modify/{id}")
    public String editNoticePage(@PathVariable Long id, Model model) {
        // 기존 상세조회 로직을 재활용해서 데이터를 가져옵니다.
        NoticeResponse notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        return "admin/notice_edit"; // notice_edit.html 파일 필요
    }

}
