package org.zerock.obj2026.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.zerock.obj2026.appointment.domain.Appointment;
import org.zerock.obj2026.mypage.dto.MyReservationDTO;
import org.zerock.obj2026.mypage.service.MypageService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/mypage")
    public String mypage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<MyReservationDTO> reservations = mypageService.getReservationsForUser(email);
        model.addAttribute(
                "reservations", reservations);
        return "mypage/mypage";
    }

    @GetMapping("/mypage/profile")
    public String profile() {
        return "mypage/profile";
    }

}
