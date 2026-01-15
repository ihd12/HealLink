package org.zerock.obj2026.admin.notice.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.obj2026.admin.notice.dto.NoticePageRequestDTO;
import org.zerock.obj2026.admin.notice.dto.NoticePageResponseDTO;
import org.zerock.obj2026.admin.notice.dto.NoticeResponse;
import org.zerock.obj2026.admin.notice.service.NoticeService;
import org.zerock.obj2026.member.dto.UserSecurityDTO;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class NoticeViewController {
    private final NoticeService noticeService;

    // 1. 목록
    @GetMapping("/admin/notice")
    public String getAllNotice(NoticePageRequestDTO pageRequestDTO, Model model) {

        // 기존(페이징x): List<NoticeResponse> list = noticeService.findAll();
        // 수정(페이징o)
        NoticePageResponseDTO<NoticeResponse> responseDTO = noticeService.findAll(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/notice";
    }

    // 2. 읽기 ->
    @GetMapping("/admin/notice/{id}")
    public String noticeDetail(@PathVariable("id") Long id,
                               NoticePageRequestDTO pageRequestDTO, // 검색/페이징 정보 추가
                               Model model) {

        // 게시글 상세 데이터 가져오기
        NoticeResponse notice = noticeService.findById(id);
        model.addAttribute("notice", notice);

        // [중요] 사용자가 보고 있던 검색/페이지 조건을 화면으로 다시 전달
        // 이렇게 하면 타임리프에서 pageRequestDTO.getLink()를 쓸 수 있습니다.
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/notice_detail";
    }



    // 3. 쓰기
    @GetMapping("/admin/notice/create")
    public String createNoticePage() {
        return "admin/notice_create";
    }

    // 4. 수정
    @GetMapping("/admin/notice/modify/{id}")
    public String editNoticePage(@PathVariable Long id, Model model,
                                 @AuthenticationPrincipal UserSecurityDTO userDetails,
                                 RedirectAttributes redirectAttributes
                                 ) {

        NoticeResponse notice = noticeService.findById(id);

        if (!notice.getWriterId().equals(userDetails.getUser().getUserId())) {
            redirectAttributes.addFlashAttribute("error", "본인의 글만 수정할 수 있습니다.");
            return "redirect:/admin/notice";
        }

        model.addAttribute("notice", notice);
        return "admin/notice_edit";
    }

}
