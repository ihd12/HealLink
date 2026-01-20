document.addEventListener("DOMContentLoaded", () => {
    // === 회원정보 수정 ===
    document.getElementById("profileBtn").addEventListener("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
        const form = document.getElementById("profileForm");
        const currentPassword = document.getElementById("currentPassword");
        // const errorPassword = document.getElementById("error-password");
        // if(currentPassword == null ||  currentPassword.trim() === ""){
        //     errorPassword.value = "비밀번호 입력";
        // }
        if (!currentPassword.value.trim()) {
            currentPassword.classList.add("error");
            const errorMsg = currentPassword.nextElementSibling;
            if (errorMsg) {
                errorMsg.textContent = "현재 비밀번호를 입력해주세요.";
                errorMsg.style.display = "block";
            }
            currentPassword.focus();
            return;
        }
        form.submit();
    });

    // === 성공 팝업 표시 ===
    const successPopup = document.getElementById("successPopup");
    if (successPopup) {
        successPopup.style.display = "flex";
    }

    // === 회원탈퇴 팝업 ===
    const withdrawBtn = document.getElementById("withdrawBtn");
    const withdrawPopup = document.getElementById("withdrawPopup");
    const withdrawCancel = document.getElementById("withdrawCancel");
    const withdrawConfirm = document.getElementById("withdrawConfirm");

    if (withdrawBtn && withdrawPopup) {
        withdrawBtn.addEventListener("click", () => withdrawPopup.style.display = "flex");
    }
    if (withdrawCancel) {
        withdrawCancel.addEventListener("click", () => withdrawPopup.style.display = "none");
    }
    if (withdrawConfirm) {
        withdrawConfirm.addEventListener("click", () => {
            /*const token = document.querySelector('meta[name="_csrf"]').content;
            const header = document.querySelector('meta[name="_csrf_header"]').content;*/
            fetch('/mypage/profile/withdraw', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    // 'X-CSRF-TOKEN': token
                }
            })
                .then(res => {
                    if (res.ok) {
                        alert("탈퇴가 완료되었습니다.");
                        window.location.href = "/"; // 탈퇴 후 홈 이동
                    } else {
                        alert("탈퇴 실패. 관리자에게 문의해주세요.");
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert("서버 요청 중 오류가 발생했습니다.");
                });
        });
    }
});

// === 팝업 닫기 공통 ===
function closePopup(id) {
    const popup = document.getElementById(id);
    if (popup) popup.style.display = "none";
}



// 내 예약 상세 캘린더
document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return;

    const selectedDate = calendarEl.dataset.date; // 예약 날짜

    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        initialDate: selectedDate,        // 예약 날짜가 있는 달로 이동
        fixedWeekCount: false,
        selectable: false,
        editable: false,
        events: selectedDate ? [
            {
                start: selectedDate,
                display: 'background',     // 날짜 강조
                backgroundColor: '#153f8f'
            }
        ] : []
    });

    calendar.render();
});