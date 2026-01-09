package org.zerock.obj2026.mainpage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.zerock.obj2026.mainpage.service.NoticeService;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final NoticeService noticeService;

    @GetMapping("/")
    public String mainpage(Model model) {

        model.addAttribute("notices", noticeService.getLatestNotices());
        return "mainpage/index";
    }
}
