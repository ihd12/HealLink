// ================== 회원정보 페이지 스크립트 ==================

document.addEventListener("DOMContentLoaded", () => {
    // 회원탈퇴 버튼 → 팝업 열기
    const withdrawBtn = document.getElementById("withdrawBtn");
    const withdrawPopup = document.getElementById("withdrawPopup");

    if (withdrawBtn && withdrawPopup) {
        withdrawBtn.addEventListener("click", () => {
            withdrawPopup.style.display = "flex";
        });
    }
    // 탈퇴 취소 버튼
    const withdrawCancel = document.getElementById("withdrawCancel");
    if (withdrawCancel) {
        withdrawCancel.addEventListener("click", () => {
            withdrawPopup.style.display = "none";
        });
    }
    // 탈퇴 확인 버튼
    const withdrawConfirm = document.getElementById("withdrawConfirm");
    if (withdrawConfirm) {
        withdrawConfirm.addEventListener("click", () => {
            alert("탈퇴가 완료되었습니다.");
            withdrawPopup.style.display = "none";
        });
    }
});

/* ================= 공통 함수 ================= */

// 회원정보 수정 유효성 검사
function submitProfile() {
    const inputs = document.querySelectorAll(".form-group input");
    let hasError = false;

    inputs.forEach(input => {
        const error = input.nextElementSibling;

        if (error && !input.value.trim()) {
            input.classList.add("error");
            error.style.display = "block";
            if (!hasError) input.focus();
            hasError = true;
        } else if (error) {
            input.classList.remove("error");
            error.style.display = "none";
        }
    });

    if (!hasError) {
        document.getElementById("successPopup").style.display = "flex";
    }
}

// 팝업 닫기 (공통)
function closePopup(id) {
    const popup = document.getElementById(id);
    if (popup) popup.style.display = "none";
}