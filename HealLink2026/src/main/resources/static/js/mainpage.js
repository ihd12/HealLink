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
});


// =========================
// 1️⃣ 헤더 애니메이션
// =========================
const header = document.getElementById('header');
let fixedAdded = false;

window.addEventListener('scroll', () => {
    const scrollY = window.scrollY;

    if (scrollY > 0 && !fixedAdded) {
        // 스크롤 시작 → fixed + 천천히 나타남
        header.classList.add('fixed');
        setTimeout(() => {
            header.classList.add('show');
        }, 50); // 애니메이션 딜레이
        fixedAdded = true;
    } else if (scrollY === 0 && fixedAdded) {
        // 최상단 → sticky로 바로 복귀 (애니메이션 없이)
        header.classList.remove('show');
        header.classList.remove('fixed');
        fixedAdded = false;
    }
});