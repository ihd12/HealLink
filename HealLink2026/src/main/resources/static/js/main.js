document.addEventListener('DOMContentLoaded', () => {
    // =========================
    // 1️⃣ 슬로건 애니메이션
    // =========================
    const titles = document.querySelectorAll('.slogan-title');
    let index = 0;

    if (titles.length > 0) { // 안전장치: 요소가 있으면 실행
        setInterval(() => {
            titles[index].classList.remove('active');
            index = (index + 1) % titles.length;
            titles[index].classList.add('active');
        }, 3000); // 3초마다 변경
    }

    // =========================
    // 2️⃣ 배너 슬라이드 애니메이션
    // =========================
    const slides = document.querySelectorAll('.banner-slide img');
    let currentSlide = 0;

    if (slides.length > 0) { // 안전장치
        // 첫 번째 이미지 표시
        slides[currentSlide].style.display = 'block';

        // 슬라이드 전환 함수
        function nextSlide() {
            slides[currentSlide].style.display = 'none'; // 현재 이미지 숨김
            currentSlide = (currentSlide + 1) % slides.length; // 다음 이미지 인덱스
            slides[currentSlide].style.display = 'block'; // 다음 이미지 표시
        }

        // 5초마다 슬라이드 전환
        setInterval(nextSlide, 5000);
    }
});