package org.zerock.obj2026.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.obj2026.member.dto.UserSecurityDTO;
import org.zerock.obj2026.mypage.dto.MypageDTO;
import org.zerock.obj2026.mypage.service.MypageService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    /* 마이페이지 - 회원정보 조회 */
    @GetMapping("/profile")
    public String profile(Model model,
                          @AuthenticationPrincipal UserSecurityDTO userSecurityDTO) {
        if(userSecurityDTO != null) {
            Long userId = userSecurityDTO.getUser().getUserId();
            MypageDTO dto = mypageService.getUserProfile(userId);
            model.addAttribute("user", userSecurityDTO.getUser());
        }
        return "mypage/profile";
    }

    /* 마이페이지 - 회원정보 수정 */
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserSecurityDTO userSecurityDTO,
                                @ModelAttribute MypageDTO dto) {

        if (userSecurityDTO != null) {
            mypageService.updateUserProfile(userSecurityDTO.getUser().getUserId(), dto);
        }
        return "redirect:/mypage/profile";
    }


    /* 마이페이지 - 내 예약 */
    @GetMapping
    public String mypage(Model model,
                         @AuthenticationPrincipal UserSecurityDTO userSecurityDTO) {

        if (userSecurityDTO != null) {
            String email = userSecurityDTO.getUser().getEmail();
            List<MypageDTO> reservations = mypageService.getReservationsForUser(email);
            model.addAttribute("reservations", reservations);
        }
        return "mypage/mypage";
    }

}
