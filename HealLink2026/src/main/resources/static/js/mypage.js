// 관리자 메뉴 아코디언 토글

document.addEventListener("DOMContentLoaded", () => {
    // 모든 관리자 메뉴 섹션 선택
    const adminSections = document.querySelectorAll(".admin-section");

    adminSections.forEach(section => {
        const title = section.querySelector(".admin-title");
        const menu = section.querySelector(".admin-menu");

        // 클릭 시 하위 메뉴 열고 닫기
        title.addEventListener("click", () => {
            menu.classList.toggle("expanded");
        });
    });
});