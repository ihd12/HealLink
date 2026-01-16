package org.zerock.obj2026.searchpharm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/searchpharm")
@PropertySource("classpath:auth.properties")
public class SearchPharmController {

    @Value("${kakao.js.key}")
    private String kakaoJsKey;

    @GetMapping("/")
    public String pharmacyMainPage(Model model) {

        model.addAttribute("kakaoJsKey", kakaoJsKey);

        return "searchpharm/main";
    }
}
