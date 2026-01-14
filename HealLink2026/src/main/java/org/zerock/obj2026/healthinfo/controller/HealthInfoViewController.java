package org.zerock.obj2026.healthinfo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.healthinfo.dto.HealthInfoResponse;
import org.zerock.obj2026.healthinfo.dto.PageRequestDTO;
import org.zerock.obj2026.healthinfo.service.HealthInfoService;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class HealthInfoViewController {
    private final HealthInfoService healthInfoService;
    
    // 목록
    @GetMapping({"/healthinfo"})
    public String getAllHealthInfo(PageRequestDTO pageRequestDTO, Model model){
        List<HealthInfoResponse> healthInfoList = healthInfoService.findAll();
        model.addAttribute("healthInfoList", healthInfoList);
        return "healthinfo";
    }

    // 읽기
    @GetMapping("/healthinfo/{id}")
    public String getHealthInfo(@PathVariable("id") long id,Model model){
        model.addAttribute("healthInfo", healthInfoService.findById(id));
        return "healthinfoview";
    }

    // 쓰기
    @GetMapping("/healthinfo/create")
    public String createForm() {
        return "healthinfocreate";
    }

    // 수정
    @GetMapping("/healthinfo/modify/{id}")
    public String modifyHealthInfo(@PathVariable Long id, Model model) {
        HealthInfoResponse response = healthInfoService.findById(id);
        model.addAttribute("healthInfo", response);
        return "healthinfomodify";
    }

}
