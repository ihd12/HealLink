package org.zerock.obj2026.healthinfo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.obj2026.healthinfo.dto.HealthInfoResponse;
import org.zerock.obj2026.healthinfo.service.HealthInfoService;


@RequiredArgsConstructor
@Controller
public class HealthInfoViewController {
    private final HealthInfoService healthInfoService;

    // 1. 목록
    @GetMapping({"/healthinfo"})
    public String getAllHealthInfo() {
        // model에 데이터를 담을 필요가 없습니다. pageable 파라미터도 자바스크립트가 관리하므로 필요 없습니다.
        return "healthinfo";
    }

    // 2. 읽기
    @GetMapping("/healthinfo/{id}")
    public String getHealthInfo(@PathVariable("id") long id,Model model){
        model.addAttribute("healthInfo", healthInfoService.findById(id));
        return "healthinfoview";
    }

    // 3. 쓰기
    @GetMapping("/healthinfo/create")
    public String createForm() {
        return "healthinfocreate";
    }

    // 4. 수정
    @GetMapping("/healthinfo/modify/{id}")
    public String modifyHealthInfo(@PathVariable Long id, Model model) {
        HealthInfoResponse response = healthInfoService.findById(id);
        model.addAttribute("healthInfo", response);
        return "healthinfomodify";
    }

}
