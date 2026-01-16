package org.zerock.obj2026.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.zerock.obj2026.member.dto.UserDTO;
import org.zerock.obj2026.member.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String join() {
        return "member/join";
    }

    @PostMapping("/join")
    public String joinProc(UserDTO dto) {
        userService.save(dto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/login/form")
    public String loginForm() {
        return "member/loginForm";
    }

}