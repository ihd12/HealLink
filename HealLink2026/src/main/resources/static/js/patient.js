document.getElementById("allergyCheck").addEventListener("click", function toggleHistory() {
    const checkBox = document.getElementById("allergyCheck");
    const historyArea = document.getElementById("historyArea");
    const allergyBox = document.getElementById("allergyBox");

    if (checkBox.checked) {
        historyArea.classList.add("show");
        allergyBox.classList.add("active");
        // 펼쳐질 때 텍스트 영역에 자동 포커스
        setTimeout(() => document.getElementById("history").focus(), 300);
    } else {
        historyArea.classList.remove("show");
        allergyBox.classList.remove("active");
        // 체크 해제 시 내용 초기화 (선택 사항)
        document.getElementById("history").value = "";
    }
})