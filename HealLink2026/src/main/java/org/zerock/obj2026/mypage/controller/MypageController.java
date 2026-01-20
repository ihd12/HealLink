package org.zerock.obj2026.mypage.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.obj2026.member.dto.UserSecurityDTO;
import org.zerock.obj2026.mypage.dto.MypageDTO;
import org.zerock.obj2026.mypage.service.MypageService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    // 회원정보 수정 페이지 로드 전 비밀번호 확인 페이지
    @GetMapping("/profile/check")
    public String profileCheckPage(@AuthenticationPrincipal UserSecurityDTO userSecurityDTO,
                                   Model model) {
        model.addAttribute("email", userSecurityDTO.getUser().getEmail());
        return "mypage/profile-check";
    }

    @PostMapping("/profile/check")
    public String checkPassword(@AuthenticationPrincipal UserSecurityDTO userSecurityDTO,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes ra) {
        // 비밀번호 일치 여부 확인
        if (!mypageService.checkPassword(userSecurityDTO.getUser(), password)) {
            ra.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/profile/check";
        }
        // 세션에 인증 완료 표시
        session.setAttribute("profileVerified", true);
        return "redirect:/mypage/profile";
    }

    // 회원정보 조회
    @GetMapping("/profile")
    public String profile(Model model,
                          @AuthenticationPrincipal UserSecurityDTO userSecurityDTO) {
        if(userSecurityDTO != null) {
            Long userId = userSecurityDTO.getUser().getUserId();
            MypageDTO dto = mypageService.getUserProfile(userId);
            model.addAttribute("user", dto);
        }
        return "mypage/profile";
    }

    // 회원정보 수정
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserSecurityDTO userSecurityDTO,
                                @ModelAttribute MypageDTO dto,
                                @RequestParam(required = false) String currentPassword,
                                Model model) {

        if (userSecurityDTO != null) {
            Long userId = userSecurityDTO.getUser().getUserId();
            MypageDTO existingUser = mypageService.getUserProfile(userId);
            model.addAttribute("user", existingUser); // 항상 user는 모델에 넣기
        }

        if (currentPassword == null || currentPassword.isBlank()) {
            model.addAttribute("error", "현재 비밀번호를 입력해주세요.");
            return "mypage/profile";
        }

        if (!mypageService.checkPassword(userSecurityDTO.getUser(), currentPassword)) {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
            return "mypage/profile";
        }

        mypageService.updateUserProfile(userSecurityDTO.getUser().getUserId(), dto);
        model.addAttribute("success", "true");
        return "mypage/profile";
    }

    // 회원 탈퇴
    @PostMapping("/profile/withdraw")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserSecurityDTO userSecurityDTO) {
        if (userSecurityDTO != null) {
            Long userId = userSecurityDTO.getUser().getUserId();
            mypageService.withdrawUser(userId); // Service에서 DB 삭제
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 내 예약 페이지
    @GetMapping
    public String mypage(Model model,
                         @AuthenticationPrincipal UserSecurityDTO userSecurityDTO,
                         @PageableDefault(
                                 size = 5,
                                 sort = "createdAt",
                                 direction = Sort.Direction.DESC
                         ) Pageable pageable) {

        if (userSecurityDTO != null) {
            String email = userSecurityDTO.getUser().getEmail();
            // 사용자 이메일 기준 예약 목록 조회 후 모델에 전달
            model.addAttribute("reservations",
                    mypageService.getReservationsForUser(email, pageable)
            );
        }
        return "mypage/mypage";
    }

    // 예약 상세 페이지
    @GetMapping("/reservation/{appointmentId}")
    public String reservationDetail(@PathVariable("appointmentId") Long appointmentId,
                                    @AuthenticationPrincipal UserSecurityDTO userSecurityDTO,
                                    Model model) {
        if (userSecurityDTO == null) {
            return "redirect:/login";   // 로그인 안 된 경우
        }
        // 예약 상세 정보 조회
        MypageDTO reservation = mypageService.getReservationDetail(appointmentId);

        // 모델에 예약 정보 전달
        model.addAttribute(
                "reservation", reservation);
        return "mypage/reservation-detail";
    }

}
