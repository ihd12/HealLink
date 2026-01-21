const state = {
    step: 1,       // 1:시도, 2:시군구, 3:읍면동
    sido: '부산',
    gu: null,
    dong: null
};

//  DOM 요소
const modalOverlay = document.getElementById('regionModalOverlay');
const container = document.getElementById('regionButtons');
const btnConfirm = document.getElementById('btnConfirm');
const stepNav = {
    1: document.getElementById('step1'),
    2: document.getElementById('step2'),
    3: document.getElementById('step3')
};

//  초기화
function initModal() {
    state.step = 1;
    state.sido = null;
    state.gu = null;
    state.dong = null;

    render();
}

//  렌더링 함수
function render() {
    container.innerHTML = "";
    updatePathUI();
    updateConfirmButton();

    let list = [];

    // 시/도 출력
    if (state.step === 1) {
        list = Object.keys(regionData);
        list.forEach(name => {
            const hasSubRegions = Object.keys(regionData[name]).length > 0;
            const btn = createBtn(name, hasSubRegions, () => {
                state.sido = name;
                state.step = 2;
                render();
            });
            container.appendChild(btn);
        });
    }
    // 시/군/구 출력
    else if (state.step === 2) {
        list = Object.keys(regionData[state.sido] || {});
        list.forEach(name => {
            const btn = createBtn(name, true, () => {
                state.gu = name;
                state.step = 3;
                render();
            });
            container.appendChild(btn);
        });
    }
    //  읍/면/동 출력
    else if (state.step === 3) {
        list = regionData[state.sido][state.gu] || [];
        if (list.length === 0) {
            container.innerHTML = "<p style='width:100%; text-align:center; color:#999;'>등록된 동 정보가 없습니다.</p>";
        }
        list.forEach(name => {
            const btn = createBtn(name, true, () => {
                state.dong = name;
                highlightBtn(btn);
                updateConfirmButton();
            });
            if (state.dong === name) btn.classList.add('active');
            container.appendChild(btn);
        });
    }
}

// 버튼 생성
function createBtn(text, enabled, onClick) {
    const btn = document.createElement("button");
    btn.className = "region-btn";
    btn.innerText = text;

    if (!enabled) {
        btn.disabled = true;
    } else {
        btn.onclick = onClick;
    }
    return btn;
}

function highlightBtn(target) {
    const btns = container.querySelectorAll('.region-btn');
    btns.forEach(b => b.classList.remove('active'));
    target.classList.add('active');
}

function updatePathUI() {
    stepNav[1].innerText = state.sido || "시/도";
    stepNav[2].innerText = state.gu || "시/군/구";
    stepNav[3].innerText = state.dong || "읍/면/동";

    for (let i = 1; i <= 3; i++) {
        stepNav[i].className = (state.step === i) ? "path-item active" : "path-item";
    }

    stepNav[1].onclick = () => {
        state.step = 1;
        state.gu = null;
        state.dong = null;
        render();
    };
    stepNav[2].onclick = () => {
        if (state.sido) {
            state.step = 2;
            state.dong = null;
            render();
        }
    };
}

// 하단 버튼
function updateConfirmButton() {
    if (state.dong) {
        btnConfirm.innerText = `"${state.dong}" 검색하기`;
        btnConfirm.disabled = false;
        btnConfirm.style.background = "#333";
    } else if (state.gu) {
        btnConfirm.innerText = `"${state.gu}" 전체 보기`;
        btnConfirm.disabled = false;
        btnConfirm.style.background = "#333";
    } else {
        btnConfirm.innerText = "지역을 선택해주세요";
        btnConfirm.disabled = true;
        btnConfirm.style.background = "#e9ecef";
    }
}

document.addEventListener("DOMContentLoaded", () => {

    const openBtn = document.getElementById("openRegionModal");
    if (openBtn) {
        openBtn.addEventListener("click", () => {
            modalOverlay.classList.add("active");
            initModal();
        });
    }

    const closeBtn = document.getElementById("closeRegionModal");
    if (closeBtn) {
        closeBtn.addEventListener("click", () => {
            modalOverlay.classList.remove("active");
        });
    }

    modalOverlay.addEventListener("click", (e) => {
        if (e.target === modalOverlay) modalOverlay.classList.remove("active");
    });

    document.getElementById("btnReset").addEventListener("click", initModal);

    btnConfirm.addEventListener("click", (e) => {
        e.preventDefault();
        e.stopPropagation();
        if (!state.sido) return;

        let url = `/hospitals/list?sido=${encodeURIComponent(state.sido)}`;
        if (state.gu) url += `&gu=${encodeURIComponent(state.gu)}`;
        if (state.dong) url += `&dong=${encodeURIComponent(state.dong)}`;

        location.href = url;
    });
});