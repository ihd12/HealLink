// 카카오 로그인 팝업
window.openKakaoLogin = function() {
    const url = "/oauth2/authorization/kakao"; // Spring Security OAuth2 카카오 로그인 엔드포인트
    const width = 500;
    const height = 600;
    const left = (window.screen.width / 2) - (width / 2);
    const top = (window.screen.height / 2) - (height / 2);

    // 팝업 열기
    const popup = window.open(
        url,
        "kakaoLoginPopup",
        `width=${width},height=${height},top=${top},left=${left},resizable=no,scrollbars=no`
    );

    // 팝업이 닫히면 부모 창 새로고침
    const timer = setInterval(() => {
        if (popup.closed) {
            clearInterval(timer);
            window.location.reload(); // 로그인 완료 후 부모 창 새로고침
        }
    }, 500);
};

document.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    if (params.has('error')) {
        alert("아이디 또는 비밀번호가 올바르지 않습니다.");

        const username = document.getElementById('username');
        const password = document.getElementById('password');

        if (username) username.value = '';
        if (password) password.value = '';
    }
});