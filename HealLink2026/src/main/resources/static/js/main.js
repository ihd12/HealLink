document.addEventListener('DOMContentLoaded', () => {
    const titles = document.querySelectorAll('.slogan-title');
    let index = 0;

    // 안전장치 (없으면 에러 방지)
    if (titles.length === 0) return;

    setInterval(() => {
        titles[index].classList.remove('active');

        index = (index + 1) % titles.length;

        titles[index].classList.add('active');
    }, 3000); // 3초마다 변경
});